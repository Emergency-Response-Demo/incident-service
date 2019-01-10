# incident-service
REST Service for the Incident Resource

To Deploy this to Openshift:
2. Change to OpenShift namespce to which you want to deploy using oc project.
2. mvn clean package fabric8:deploy -Popenshift

The maven command above will accomplish the following:
1. Maven will build the code locally, outputting a jar in the target/ directory.
2. The fabric8 plugin will create a BuildController, DeploymentController, ImageStream, Service and Route in OpenShift. The BuildController defines an S2I Binary build that takes the jar as input.
3. The fabric8 plugin will start an OpenShift Build using the BuildController. 
4. The Build will push to the ImageStream, the DeploymentController will trigger, and a Pod will deploy.
