package com.family.demotest.common.sql;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import com.family.base01.util.Logger;
import com.family.base01.util.Validator;

/**
 * @author wujf
 */
public class SqlSupport
{
    @Autowired
    protected Validator validator;
    @Autowired
    protected Logger logger;



    
	/**
	 * 批量插入
	 * @param sql
	 * @param args
	 */
    protected boolean insertBatch(DataSource dataSource,String sql, List<Object[]> args)
    {
        if(logger.isDebugEnable())
            logger.debug("执行SQL批量插入"+sql);

        if(validator.isBlank(sql)||validator.isEmpty(args))
            return true;

        Connection connection = null;
        try
        {
            connection = dataSource.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            for(int i = 0; i<args.size(); i++)
            {
                for(int j = 0; j<args.get(i).length; j++)
                    pstmt.setObject(j+1, args.get(i)[j]);
                pstmt.addBatch();
            }
            
            int[] results =  pstmt.executeBatch();
            
            pstmt.close();
            pstmt = null;
            
            int succCnt = 0;
            for (int result: results) {
                if (result >= 0) {
                    succCnt++;
                }
            }
            
            boolean succ = succCnt == args.size();         
            return succ;

        }
        catch(SQLException e)
        {
            logger.warn(e, "执行SQL["+sql+"]批量插入时发生异常！");
            return false;
        }
        finally
        {
            close(connection);
        }
    }
    
    
    protected boolean update(DataSource dataSource, String sql, Object[] args)
    {
        if(logger.isDebugEnable())
            logger.debug("执行SQL更新"+sql);

        if(validator.isBlank(sql))
            return false;

        Connection connection = null;
        try
        {
            connection = dataSource.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            if(!validator.isEmpty(args))
                for(int i = 0; i<args.length; i++)
                    pstmt.setObject(i+1, args[i]);
            pstmt.executeUpdate();
            pstmt.close();
            pstmt = null;
        }
        catch(SQLException e)
        {
            logger.warn(e, "执行SQL["+sql+"]更新时发生异常！");
            return false;
        }
        finally
        {
            close(connection);
        }
        return true;
    }

    protected void close(Connection connection)
    {
        try
        {
            if(!connection.isClosed())
                connection.close();
        }
        catch(Exception e)
        {
            logger.warn(e, "关闭数据库连接时发生异常！");
        }
    }
}
