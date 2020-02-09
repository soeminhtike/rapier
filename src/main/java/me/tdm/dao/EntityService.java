package me.tdm.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import me.tdm.entity.PredefinedTag;
import me.tdm.entity.RapierRule;

@Repository
@Transactional(readOnly = true)
public class EntityService {
	
	private static Logger logger = Logger.getLogger(EntityService.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Transactional(readOnly = false)
	public void save(PredefinedTag tag) {
		sessionFactory.getCurrentSession().save(tag);
	}
	
	@Transactional(readOnly=false)
	public void save(RapierRule rule) {
		sessionFactory.getCurrentSession().save(rule);
	}
	
	public List<RapierRule> getAllRapierRule() {
		return sessionFactory.getCurrentSession().createQuery("from RaperRule r").list();
	}
	
	public List<PredefinedTag> getAllPredefinedTag() {
		List<PredefinedTag> tagList = sessionFactory.getCurrentSession().createQuery("from PredefinedTag pt").list();
		return tagList;
	}
}
