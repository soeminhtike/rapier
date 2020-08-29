package me.tdm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tdm.entity.BaseEntity;
import me.tdm.entity.Prefiller;
import me.tdm.entity.Rule;
import me.tdm.entity.Tag;

@Repository
@Transactional(readOnly = true)
public class EntityService {

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(readOnly = false)
	public void save(BaseEntity entity) {
		sessionFactory.getCurrentSession().saveOrUpdate(entity);
	}

	@Transactional(readOnly = false)
	public void savePrefiller(Prefiller prefiller) {
		save(prefiller);
	}

	@SuppressWarnings("unchecked")
	public List<Rule> getAllRapierRule() {
		return sessionFactory.getCurrentSession().createQuery("from Rule r").list();
	}

	@SuppressWarnings("unchecked")
	public List<Tag> getAllPredefinedTag() {
		List<Tag> tagList = sessionFactory.getCurrentSession().createQuery("from Tag pt").list();
		return tagList;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> findByString(String queryString, Object value, Class<T> t) {
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(queryString);
		query.setParameter("dataInput", value);
		return query.list();
	}
}
