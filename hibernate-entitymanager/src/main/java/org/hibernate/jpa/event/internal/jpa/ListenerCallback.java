/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.jpa.event.internal.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hibernate.jpa.event.spi.jpa.Callback;

/**
 * Represents a JPA callback using a dedicated listener
 *
 * @author <a href="mailto:kabir.khan@jboss.org">Kabir Khan</a>
 * @author Steve Ebersole
 */
public class ListenerCallback implements Callback {
	private final Method callbackMethod;
	private final Object listenerInstance;

	public ListenerCallback(Object listenerInstance, Method callbackMethod) {
		this.listenerInstance = listenerInstance;
		this.callbackMethod = callbackMethod;
	}

	@Override
	public boolean performCallback(Object entity) {
		try {
			callbackMethod.invoke( listenerInstance, entity );
			return true;
		}
		catch (InvocationTargetException e) {
			//keep runtime exceptions as is
			if ( e.getTargetException() instanceof RuntimeException ) {
				throw (RuntimeException) e.getTargetException();
			}
			else {
				throw new RuntimeException( e.getTargetException() );
			}
		}
		catch (Exception e) {
			throw new RuntimeException( e );
		}
	}

	@Override
	public boolean isActive() {
		return true;
	}
}