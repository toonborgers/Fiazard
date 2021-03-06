package be.swsb.fiazard.main;

import be.swsb.fiazard.common.exceptions.FiazardExceptionToJSONMapper;
import be.swsb.fiazard.ordering.domain.category.CategoryDAO;
import be.swsb.dropwizard.healthchecks.MongoDBHealthCheck;
import be.swsb.fiazard.management.domain.topping.ToppingDAO;
import be.swsb.fiazard.ordering.resource.CategoryResourceV1;
import be.swsb.fiazard.ordering.resource.OpeningHourResourceV1;
import be.swsb.fiazard.ordering.resource.ProductResourceV1;
import be.swsb.fiazard.management.resource.ToppingResourceV1;
import com.commercehub.dropwizard.mongo.ManagedMongoClient;
import com.mongodb.DB;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.net.UnknownHostException;

import static be.swsb.fiazard.util.representation.FiazardJacksonModule.MODULE;

public class FiazardApp extends Application<FiazardConfig> {

    @Override
    public String getName() {
        return "Fiazardtje";
    }

    @Override
    public void initialize(Bootstrap<FiazardConfig> bootstrap) {
    }

    @Override
    public void run(FiazardConfig config, Environment environment) throws Exception {
        DB db = configureMongo(config, environment);

        environment.jersey().register(FiazardExceptionToJSONMapper.class);
        environment.jersey().register(new CategoryResourceV1(new CategoryDAO(db)));
        environment.jersey().register(new ProductResourceV1());
        environment.jersey().register(new OpeningHourResourceV1());
        environment.jersey().register(new ToppingResourceV1(new ToppingDAO(db)));

        environment.getObjectMapper().registerModule(MODULE);
    }

    //TODO move to a MongoDB Module
    private DB configureMongo(FiazardConfig config, Environment environment) throws UnknownHostException {
        ManagedMongoClient mongoClient = config.getMongo().build();
        DB db = mongoClient.getDB(config.getMongo().getDbName());
        environment.lifecycle().manage(config.getMongo().build());
        environment.healthChecks().register("MongoDBHealthCheck", new MongoDBHealthCheck(db));
        return db;
    }

    public static void main(String[] args) throws Exception {
        new FiazardApp().run(args);
    }
}
