/*
 * Copyright (C) 2005 Alfresco, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.alfresco.benchmark.framework;

import java.util.List;

import org.alfresco.benchmark.framework.dataprovider.RepositoryProfile;

/**
 * @author Roy Wetherall
 */
public interface DataLoaderComponent
{
    public static final String BENCHMARK_OBJECT_PREFIX = "bm_";
    
    /**
     * Load data into the repository.  A new folder is created into which the
     * new data is looaded.  The respoitory profile is used to determine
     * the structure and size of the data loaded.
     * 
     * @param repositoryProfile     the repository profile
     * @return                      detais of the loaded data
     */
    public LoadedData loadData(RepositoryProfile repositoryProfile);
    
    /**
     * Create a number of test users
     * 
     * @param count     the number of users to create
     */
    public List<String> createUsers(int count);
}
