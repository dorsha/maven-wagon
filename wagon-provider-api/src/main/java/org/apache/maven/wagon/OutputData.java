package org.apache.maven.wagon;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.wagon.resource.Resource;

import java.io.OutputStream;

/**
 * @author <a href="mailto:michal@codehaus.org">Michal Maczka</a>
 * @version $Id$
 */
public class OutputData
{
    private OutputStream outputStream;

    private Resource resource;

    public OutputStream getOutputStream()
    {
        return outputStream;
    }

    public void setOutputStream( OutputStream outputStream )
    {
        this.outputStream = outputStream;
    }

    public Resource getResource()
    {
        return resource;
    }

    public void setResource( Resource resource )
    {
        this.resource = resource;
    }
}