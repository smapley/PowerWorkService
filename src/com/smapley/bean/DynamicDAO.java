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
 * Dynamic entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.smapley.bean.Dynamic
 * @author MyEclipse Persistence Tools
 */
@Transactional
public class DynamicDAO {
	private static final Logger log = LoggerFactory.getLogger(DynamicDAO.class);
	// property constants
	public static final String TYPE = "type";
	public static final String PIC_URL = "picUrl";
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

	public void save(Dynamic transientInstance) {
		log.debug("saving Dynamic instance");
		try {
			getCurrentSession().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Dynamic persistentInstance) {
		log.debug("deleting Dynamic instance");
		try {
			getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Dynamic findById(java.lang.Integer id) {
		log.debug("getting Dynamic instance with id: " + id);
		try {
			Dynamic instance = (Dynamic) getCurrentSession().get(
					"com.smapley.bean.Dynamic", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List<Dynamic> findByExample(Dynamic instance) {
		log.debug("finding Dynamic instance by example");
		try {
			List<Dynamic> results = (List<Dynamic>) getCurrentSession()
					.createCriteria("com.smapley.bean.Dynamic")
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
		log.debug("finding Dynamic instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Dynamic as model where model."
					+ propertyName + "= ?";
			Query queryObject = getCurrentSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List<Dynamic> findByType(Object type) {
		return findByProperty(TYPE, type);
	}

	public List<Dynamic> findByPicUrl(Object picUrl) {
		return findByProperty(PIC_URL, picUrl);
	}

	public List<Dynamic> findByState(Object state) {
		return findByProperty(STATE, state);
	}

	public List findAll() {
		log.debug("finding all Dynamic instances");
		try {
			String queryString = "from Dynamic";
			Query queryObject = getCurrentSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Dynamic merge(Dynamic detachedInstance) {
		log.debug("merging Dynamic instance");
		try {
			Dynamic result = (Dynamic) getCurrentSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Dynamic instance) {
		log.debug("attaching dirty Dynamic instance");
		try {
			getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Dynamic instance) {
		log.debug("attaching clean Dynamic instance");
		try {
			getCurrentSession().buildLockRequest(LockOptions.NONE).lock(
					instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static DynamicDAO getFromApplicationContext(ApplicationContext ctx) {
		return (DynamicDAO) ctx.getBean("DynamicDAO");
	}
}