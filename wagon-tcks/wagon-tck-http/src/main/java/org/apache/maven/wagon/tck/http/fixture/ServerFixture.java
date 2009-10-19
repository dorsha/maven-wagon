package org.apache.maven.wagon.tck.http.fixture;

import static org.apache.maven.wagon.tck.http.util.TestUtil.getResource;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.DefaultHandler;
import org.mortbay.jetty.handler.HandlerCollection;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.security.Constraint;
import org.mortbay.jetty.security.ConstraintMapping;
import org.mortbay.jetty.security.HashUserRealm;
import org.mortbay.jetty.security.SecurityHandler;
import org.mortbay.jetty.security.SslSocketConnector;
import org.mortbay.jetty.servlet.AbstractSessionManager;
import org.mortbay.jetty.servlet.FilterHolder;
import org.mortbay.jetty.servlet.FilterMapping;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.servlet.SessionHandler;
import org.mortbay.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.Servlet;

public class ServerFixture
{
    public static final String SERVER_ROOT_RESOURCE_PATH = "default-server-root";

    public static final String SERVER_SSL_KEYSTORE_RESOURCE_PATH = "ssl/keystore";

    public static final String SERVER_SSL_KEYSTORE_PASSWORD = "password";

    public static final String SERVER_HOST = "localhost";

    private final Server server;

    private final WebAppContext webappContext;

    private final HashUserRealm securityRealm;

    private final SecurityHandler securityHandler;

    private int filterCount = 0;;

    public ServerFixture( final int port, final boolean ssl )
        throws URISyntaxException, IOException
    {
        server = new Server();
        if ( ssl )
        {
            SslSocketConnector connector = new SslSocketConnector();
            String keystore = getResource( SERVER_SSL_KEYSTORE_RESOURCE_PATH ).getAbsolutePath();

            // connector.setHost( SERVER_HOST );
            connector.setPort( port );
            connector.setKeystore( keystore );
            connector.setPassword( SERVER_SSL_KEYSTORE_PASSWORD );
            connector.setKeyPassword( SERVER_SSL_KEYSTORE_PASSWORD );

            server.addConnector( connector );
        }
        else
        {
            Connector connector = new SelectChannelConnector();
            connector.setHost( "localhost" );
            connector.setPort( port );
            server.addConnector( connector );
        }

        Constraint constraint = new Constraint();
        constraint.setName( Constraint.__BASIC_AUTH );

        constraint.setRoles( new String[] { "allowed" } );
        constraint.setAuthenticate( true );

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint( constraint );
        cm.setPathSpec( "/protected/*" );

        securityHandler = new SecurityHandler();

        securityRealm = new HashUserRealm( "Test Server" );

        securityHandler.setUserRealm( securityRealm );
        securityHandler.setConstraintMappings( new ConstraintMapping[] { cm } );

        webappContext = new WebAppContext();
        webappContext.setContextPath( "/" );

        File base = getResource( SERVER_ROOT_RESOURCE_PATH );
        System.out.println( "docroot: " + base );
        webappContext.setWar( base.getAbsolutePath() );
        webappContext.addHandler( securityHandler );

        SessionHandler sessionHandler = webappContext.getSessionHandler();
        ( (AbstractSessionManager) sessionHandler.getSessionManager() ).setUsingCookies( false );

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers( new Handler[] { webappContext, new DefaultHandler() } );

        server.setHandler( handlers );
    }

    public void addFilter( final String pathSpec, final Filter filter )
    {
        String name = "filter" + filterCount++;

        FilterMapping fm = new FilterMapping();
        fm.setPathSpec( pathSpec );
        fm.setFilterName( name );

        FilterHolder fh = new FilterHolder( filter );
        fh.setName( name );

        webappContext.getServletHandler().addFilter( fh, fm );
    }

    public void addServlet( final String pathSpec, final Servlet servlet )
    {
        webappContext.getServletHandler().addServletWithMapping( new ServletHolder( servlet ), pathSpec );
    }

    public void addUser( final String user, final String password )
    {
        securityRealm.put( user, password );
        securityRealm.addUserToRole( user, "allowed" );
    }

    public Server getServer()
    {
        return server;
    }

    public WebAppContext getWebappContext()
    {
        return webappContext;
    }

    public void stop()
        throws Exception
    {
        if ( server != null )
        {
            server.stop();
        }
    }

    public void start()
        throws Exception
    {
        server.start();

        int total = 0;
        while ( total < 3000 && !server.isStarted() )
        {
            server.wait( 10 );
            total += 10;
        }

        if ( !server.isStarted() )
        {
            throw new IllegalStateException( "Server didn't start in: " + total + "ms." );
        }
    }

}