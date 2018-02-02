package ru.otus.kirillov.myorm.commands.saveOrUpdate;

import ru.otus.kirillov.model.DataSet;
import ru.otus.kirillov.myorm.commands.Request;
import ru.otus.kirillov.myorm.shema.EntityDescriptor;
import ru.otus.kirillov.utils.CommonUtils;

/**
 * Created by Александр on 01.02.2018.
 */
public class SaveOrUpdateRequest extends Request {

    EntityDescriptor descriptor;

    DataSet object;

    public SaveOrUpdateRequest(EntityDescriptor descriptor, DataSet object) {
        super(Type.SAVE_OR_UPDATE);
        this.descriptor = CommonUtils.retunIfNotNull(descriptor);
        this.object = CommonUtils.retunIfNotNull(object);
    }

    public EntityDescriptor getDescriptor() {
        return descriptor;
    }

    public DataSet getObject() {
        return object;
    }
}
