package com.ifpb.lattesmaismais;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@SelectPackages({"com.ifpb.lattesmaismais.business", "com.ifpb.lattesmaismais.presentation.dto"})
@Suite
@SuiteDisplayName("Tests for functionalities implemented in Sprint 1")
class LattesMaisMaisApplicationTests {
}
