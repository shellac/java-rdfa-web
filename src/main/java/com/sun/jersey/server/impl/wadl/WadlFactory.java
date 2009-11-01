package com.sun.jersey.server.impl.wadl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.model.AbstractResource;
import com.sun.jersey.core.spi.factory.InjectableProviderFactory;
import com.sun.jersey.server.impl.model.method.ResourceMethod;
import com.sun.jersey.server.impl.uri.PathPattern;

public class WadlFactory {

	public WadlFactory(ResourceConfig resourceConfig) {
	}

	public boolean isSupported() {
		return false;
	}

	public void init(InjectableProviderFactory ipf, Set<AbstractResource> rootResources) {
	}

	public ResourceMethod createWadlOptionsMethod(Map<String, List<ResourceMethod>> methods, AbstractResource resource, PathPattern p) {
		return null;
	}

}
