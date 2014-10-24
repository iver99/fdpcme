environments {
	test {
    sonarHost     = 'http://slc07dsz.us.oracle.com:9000'
    sonarDB       = 'jdbc:h2:tcp://slc07dsz.us.oracle.com:9092/sonar'
    sonarLogin    = 'uploader'
    sonarPassword = 'uploader1'
	}

	prod {
    sonarHost     = 'http://slc06pqa.us.oracle.com:9000'
    sonarDB       = 'jdbc:h2:tcp://slc06pqa.us.oracle.com:9092/sonar'
    sonarLogin    = 'uploader'
    sonarPassword = 'uploader1'
	}
}
