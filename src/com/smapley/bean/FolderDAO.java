package com.smapley.bean;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Example.create;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * A data access object (DAO) providing persistence and search support for
 * Folder entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.smapley.bean.Folder
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class FolderDAO {
	private static final Logger log = LoggerFactory.getLogger(FolderDAO.class);
	// property constants
	public static final String NAME = "name";
	public static final String STATE = "state";

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	protected void initDao() {
		// do nothing
	}

	public void save(Folder transientInstance) {
		log.debug("saving Folder instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Folder persistentInstance) {
		log.debug("deleting Folder instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Folder findById(java.lang.Integer id) {
		log.debug("getting Folder instance with id: " + id);
		try {
			Folder instance = (Folder) getCurrentSession().get(
					"com.smapley.bean.Folder", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Folder> findByExample(Folder instance) {
		log.debug("finding Folder instance by example");
		try {
			List<Folder> results = (List<Folder>) getCurrentSession()
					.createCriteria("com.smapley.bean.Folder")
					.add(create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Folder instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Folder as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Folder> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<Folder> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findAll() {
		log.debug("finding all Folder instances");
		try {
			String queryString = "from Folder";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Folder merge(Folder detachedInstance) {
		log.debug("merging Folder instance");
		try {
			Folder result = (Folder) getCurrentSession()
					.merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Folder instance) {
		log.debug("attaching dirty Folder instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Folder instance) {
		log.debug("attaching clean Folder instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static FolderDAO getFromApplicationContext(ApplicationContext ctx) {
		return (FolderDAO) ctx.getBean("FolderDAO");
	}
}