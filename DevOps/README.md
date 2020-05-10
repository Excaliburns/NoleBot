#DEVOPS

##CI/CD Process
There is a jenkins instance sitting on the same aws account as NoleBot.
	*This instance will have a different IP if it is relaunched because aws EC2 dynamically gives instances IPv4 addresses.
	*Github webhook sends a push to jenkins that includes a zipped up version of whatever is on GitHub.

	*This instance builds the new jar using gradle.
	
	*Uses Scp to overwrite the jar in the NoleBot Ec2.

	*Then runs the script in this folder to kill all the other instances of the jar and run the current one.
