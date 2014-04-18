package br.usp.ime.escience.expressmatch.model.dao.generic;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public abstract class GenericDao<T, Pk>{

    @PersistenceContext
    protected EntityManager em;

    private Class<T> type;

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public GenericDao() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        type = (Class) pt.getActualTypeArguments()[0];
    }

    public T create(final T t) {
        this.em.persist(t);
        return t;
    }

    public void delete(final Pk id) {
        this.em.remove(this.em.getReference(type, id));
    }

    public T find(final Pk id) {
        return (T) this.em.find(type, id);
    }

    public T update(final T t) {
        return this.em.merge(t);    
    }
}
