/* Copyright (c) 2000-2003, jMock.org. See LICENSE.txt */
package org.jmock.util;

import org.jmock.core.CoreMock;
import org.jmock.core.DynamicUtil;
import org.jmock.core.Invocation;
import org.jmock.core.InvocationMocker;
import org.jmock.core.matcher.StatelessInvocationMatcher;
import org.jmock.core.stub.CustomStub;

public class Dummy {
	private Dummy() {
	    /* This class cannot be instantiated. */
	}

	public static Object newDummy(Class interfaceClass) {
		return newDummy( interfaceClass, "dummy"+ DynamicUtil.classShortName(interfaceClass) );
	}

	public static Object newDummy( final Class interfaceClass, final String name ) {
		CoreMock mock = new CoreMock( interfaceClass, name );
        InvocationMocker mocker = new InvocationMocker();
        
        mocker.addMatcher( new StatelessInvocationMatcher() {
            public boolean matches( Invocation invocation ) {
            	return invocation.getDeclaringClass() == interfaceClass;
            }
            public StringBuffer describeTo(StringBuffer buf) {
            	return buf.append("anu method declared in " + interfaceClass);
            }
        } );
        mocker.setStub( new CustomStub("dummy method") {
        	public Object invoke( Invocation invocation ) throws Throwable {
                throw new NotImplementedException( 
                    invocation.getMethodName() + "called on " + name );
            }
        } );
        
        mock.add(mocker);
        
        return mock.proxy();
	}

	public static Object newDummy( final String name ) {
        return new Object() {
        	public String toString() {
        		return name;
            }
        };
	}
}