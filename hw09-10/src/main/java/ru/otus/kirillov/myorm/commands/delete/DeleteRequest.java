package ru.otus.kirillov.myorm.commands.delete;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.commands.Request;
import ru.otus.kirillov.myorm.shema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.shema.elements.EntityDescriptor;

import java.util.List;

/**
 * Created by Александр on 31.01.2018.
 */
public class DeleteRequest extends Request {

    private EntityDescriptor entityDescriptor;

    private List<Pair<AbstractFieldDescriptor, Object>> whereClojure;

    private boolean needToCommit;

    public DeleteRequest() {
        super(Type.DELETE);
    }

    public DeleteRequest(EntityDescriptor entityDescriptor, List<Pair<AbstractFieldDescriptor, Object>> whereClojure) {
        this(entityDescriptor, whereClojure, true);
    }

    public DeleteRequest(EntityDescriptor entityDescriptor, List<Pair<AbstractFieldDescriptor, Object>> whereClojure,
                         boolean needToCommit) {
        this();
        this.entityDescriptor = entityDescriptor;
        this.whereClojure = whereClojure;
        this.needToCommit = needToCommit;
    }

    public EntityDescriptor getEntityDescriptor() {
        return entityDescriptor;
    }

    public List<Pair<AbstractFieldDescriptor, Object>> getWhereClojure() {
        return whereClojure;
    }

    public boolean isNeedToCommit() {
        return needToCommit;
    }
}
