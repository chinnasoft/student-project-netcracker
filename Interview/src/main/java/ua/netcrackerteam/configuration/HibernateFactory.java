package ua.netcrackerteam.configuration;

import ua.netcrackerteam.DAO.*;

/**
 * @author
 */
public class HibernateFactory {
    
    private static DAOCommon commonDao = null;
    private static DAOStudentImpl studentDAO = null;    
    private static InterviewDAOImpl interviewDAO = null;
        
    private static HibernateFactory instance = null;

    public static synchronized HibernateFactory getInstance(){
        if (instance == null){
            instance = new HibernateFactory();
        }
        return instance;
    }

    public DAOCommon getCommonDao(){
        if(commonDao == null){
            commonDao = new DAOCommon();
        }
        return commonDao;
    }

    public DAOStudentImpl getStudentDAO(){
        if (studentDAO == null){
            studentDAO = new DAOStudentImpl();
        }
        return studentDAO;
    }
    
    public InterviewDAOImpl getDAOInterview(){
        if (interviewDAO == null){
            interviewDAO = new InterviewDAOImpl();
        }
        return interviewDAO;
    }
    

}
