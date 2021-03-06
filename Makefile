#include ../../../Makefile.common
include ../Makefile.common
#
# Specfic options
#
RTML_NAME	=rtml
PACKAGEDIR 	=org/$(ESTAR_NAME)/$(RTML_NAME)
PACKAGENAME	=org.$(ESTAR_NAME).$(RTML_NAME)
JAR_FILE	=org_$(ESTAR_NAME)_$(RTML_NAME).jar
JAVACFLAGS 	=$(JAVAC_VERSION_FLAGS) -d $(LIBDIR) -sourcepath ../../../ -classpath $(LIBDIR):$(CLASSPATH)
DOCSDIR 	= $(ESTAR_DOC_HOME)/javadocs/$(PACKAGEDIR)

SRCS = RTMLException.java RTMLErrorHandler.java RTMLDateFormat.java RTMLPeriodFormat.java RTMLAttributes.java \
	RTMLDeviceHolder.java RTMLTargetHolder.java RTMLIntelligentAgent.java \
	RTMLTarget.java RTMLEphemerisTargetTrackNode.java \
	RTMLSeeingConstraint.java RTMLSeriesConstraint.java RTMLMoonConstraint.java RTMLSkyConstraint.java \
	RTMLExtinctionConstraint.java RTMLAirmassConstraint.java \
	RTMLSchedule.java RTMLGrating.java RTMLHalfWavePlate.java RTMLDevice.java \
	RTMLDetector.java RTMLImageData.java RTMLObservation.java \
	RTMLTelescope.java RTMLTelescopeLocation.java \
	RTMLDocument.java RTMLParser.java RTMLCreate.java RTMLContact.java RTMLProject.java RTMLScore.java \
	RTML22Parser.java RTML31Parser.java RTMLHistory.java RTMLHistoryEntry.java RTML22Create.java RTML31Create.java 
OBJS = $(SRCS:%.java=$(LIBDIR)/$(PACKAGEDIR)/%.class)
DOCS = $(SRCS:%.java=$(DOCSDIR)/$(PACKAGEDIR)/%.html)
#CONFIGS = xml_environment.csh
#CONFIGSBIN = $(CONFIGS:%=$(LIBDIR)/%)

DIRS = test
# configs
top: jar
	@for i in $(DIRS); \
	do \
		(echo making in $$i...; cd $$i; $(MAKE) ); \
	done;

$(LIBDIR)/$(PACKAGEDIR)/%.class: %.java
	$(JAVAC) $(JAVAC_OPTIONS) $(JAVACFLAGS) $<
jar: $(JARLIBDIR)/$(JAR_FILE)

$(JARLIBDIR)/$(JAR_FILE): $(OBJS)
	(cd $(LIBDIR); $(JAR) $(JAR_OPTIONS) $(JAR_FILE) $(PACKAGEDIR); $(MV) $(JAR_FILE) $(JARLIBDIR))

docs: $(DOCS)
	@for i in $(DIRS); \
	do \
		(echo docs in $$i...; cd $$i; $(CO) $(CO_OPTIONS) Makefile; $(MAKE) docs); \
	done;

$(DOCSDIR)/$(PACKAGEDIR)/%.html: %.java
	$(JAVADOC) -sourcepath ../../..:$(CLASSPATH) -d $(DOCSDIR) $(DOCFLAGS) $(PACKAGENAME)

#configs: $(CONFIGSBIN)

#$(LIBDIR)/%: %
#	$(CP) $< $@

checkout:
	$(CO) $(CO_OPTIONS) $(SRCS)
	@for i in $(DIRS); \
	do \
		(echo checkout in $$i...; cd $$i; $(CO) $(CO_OPTIONS) Makefile; $(MAKE) checkout); \
	done;

checkin:
	-$(CI) $(CI_OPTIONS) $(SRCS)
	-@for i in $(DIRS); \
	do \
		(echo checkin in $$i...; cd $$i; $(MAKE) checkin; $(CI) $(CI_OPTIONS) Makefile); \
	done;

depend:
	echo "No depend."

clean:
	-$(RM) $(RM_OPTIONS) $(OBJS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo cleaning in $$i...; cd $$i; $(MAKE) clean); \
	done;

tidy:
	-$(RM) $(RM_OPTIONS) $(TIDY_OPTIONS)
	@for i in $(DIRS); \
	do \
		(echo cleaning in $$i...; cd $$i; $(MAKE) tidy); \
	done;

backup: tidy checkin
	-$(RM) $(RM_OPTIONS) $(OBJS)
	@for i in $(DIRS); \
	do \
		(echo backup in $$i...; cd $$i; $(MAKE) backup); \
	done;
	$(TAR) cvf $(BACKUP_DIR)/org_$(ESTAR_NAME)_$(RTML_NAME).tar .
	$(COMPRESS) $(BACKUP_DIR)/org_$(ESTAR_NAME)_$(RTML_NAME).tar
