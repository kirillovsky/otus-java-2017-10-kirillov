package ru.otus.kirillov.myorm.commands.select;

import org.apache.commons.lang3.tuple.Pair;
import ru.otus.kirillov.myorm.commands.Request;
import ru.otus.kirillov.myorm.schema.elements.AbstractFieldDescriptor;
import ru.otus.kirillov.myorm.schema.elements.EntityDescriptor;

import java.util.List;

/**
 * Created by Александр on 30.01.2018.
 */
public class SelectRequest extends Request {

    private EntityDescriptor entityDescriptor;

    private List<Pair<AbstractFieldDescriptor, Object>>  whereClojure;

    private SelectRequest() {
        super(Type.SELECT);
    }

    public SelectRequest(EntityDescriptor entityDescriptor, List<Pair<AbstractFieldDescriptor, Object>> whereClojure) {
        this();
        this.entityDescriptor = entityDescriptor;
        this.whereClojure = whereClojure;
    }

    public EntityDescriptor getEntityDescriptor() {
        return entityDescriptor;
    }

    public List<Pair<AbstractFieldDescriptor, Object>>  getWhereClojure() {
        return whereClojure;
    }
}
