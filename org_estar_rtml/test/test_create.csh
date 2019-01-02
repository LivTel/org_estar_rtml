#!/bin/csh
java org.estar.rtml.test.TestCreate -request -iahost localhost -iaid 1 -iaport 1234 -device ratcam camera optical R -observation -name test -target_ident test-ident -ra 01:02:03 -dec +45:56:01 -exposure 1000 ms 1 -start_date 2005-01-01T12:00:00 -end_date 2031-12-31T12:00:00 -series_constraint_count 3 -series_constraint_interval PT1H -series_constraint_tolerance PT30M -device ratcam camera optical V -binning 2 2 -project "agent_test" -contact -contact_name "Chris Mottram" -contact_user TMC/estar 
