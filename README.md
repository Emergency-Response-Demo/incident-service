# Incident Service for the Red Hat Cajun Navy Demo 

## Building
* Update your Maven settings.xml to use the team Nexus server by adding the following profile:
```
    <profiles>
        <profile>
            <id>openshift</id>
            <repositories>
                <repository>
                    <id>team.nexus</id>
                    <name>Red Hat Cajun Navy Demo Team Nexus</name>
                    <releases>
                        <enabled>false</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>warn</checksumPolicy>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                        <updatePolicy>never</updatePolicy>
                        <checksumPolicy>fail</checksumPolicy>
                    </snapshots>
                    <url>https://nexus-nexus.apps.753d.openshift.opentlc.com/nexus/content/repositories/releases/</url>
                    <layout>default</layout>
                </repository>
            </repositories>
        </profile>
    </profiles>
```
* `mvn clean package -Popenshift`

## Deploying to OpenShift
1. Configure Maven to use Nexus as described above.
2. `oc login`
3. `oc project <your project>`
4. `mvn clean package fabric8:deploy -D fabric8.namespace=naps-emergency-response -Popenshift`

   The `-D fabric8-namspace` argument is optional. If left out, the fabric8:deploy plugin will deploy to the active OpenShift project.

The maven command above will do the following:
1. Maven will build the code locally, outputting a jar in the target/ directory.
2. On the first run, the fabric8 plugin will create a BuildController, DeploymentController, ImageStream, Service and Route in OpenShift.

   The BuildController defines an S2I Binary build that takes the jar as input.
  
3. The fabric8 plugin will start an OpenShift Build using the BuildController. 
4. The Build will push to the ImageStream, which will trigger the DeploymentController will trigger to deploy a Pod running the code.
