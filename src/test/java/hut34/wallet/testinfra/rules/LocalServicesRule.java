package hut34.wallet.testinfra.rules;

import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import hut34.wallet.framework.config.ObjectifyConfig;
import org.junit.rules.ExternalResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalServicesRule extends ExternalResource {
    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
        new LocalTaskQueueTestConfig());
    private Closeable ofyService;
    private List<Class<?>> entities;

    /**
     * By default this will register all entities as defined in {@link ObjectifyConfig}.
     */
    public LocalServicesRule() {
        this(new ObjectifyConfig().registerObjectifyEntities().toArray(new Class[0]));
    }

    public LocalServicesRule(Class<?>... entities) {
        this.entities = Stream.of(entities).collect(Collectors.toList());
    }

    public void registerAdditionalEntities(Class<?>... entities) {
        for (Class<?> entity : entities) {
            ObjectifyService.register(entity);
            this.entities.add(entity);
        }
    }

    protected void before() {
        helper.setUp();
        ofyService = ObjectifyService.begin();

        for(Class<?> entity: entities) {
            ObjectifyService.register(entity);
        }

    }

    protected void after() {
        helper.tearDown();
        ofyService.close();
    }

    public QueueStateInfo getTaskQueue(String queueName) {
        return LocalTaskQueueTestConfig.getLocalTaskQueue().getQueueStateInfo().get(queueName);
    }

}
