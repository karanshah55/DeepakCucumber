/*
 * This class is designed for better cucumber HTML report formating.
 * It formating feature file test, scenario outline, test steps with creating of local thread
 */
package com.cucumber.listener;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;

/**
 * This class if designed for better format of cucumber HTML report output
 *
 */
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Background;
import gherkin.formatter.model.DataTableRow;
import gherkin.formatter.model.Examples;
import gherkin.formatter.model.ExamplesTableRow;
import gherkin.formatter.model.Feature;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Scenario;
import gherkin.formatter.model.ScenarioOutline;
import gherkin.formatter.model.Step;
import gherkin.formatter.model.Tag;

/**
 * A cucumber based reporting listener which generates the Extent Report.
 * This class is designed for better cucumber HTML report formating.
 * It formating feature file test, scenario outline, test steps with creating of local thread
 */
public class ExtentCucumberFormatter implements Reporter, Formatter {

	/** The extent reports. */
	private static ExtentReports extentReports;

	/** The html reporter. */
	private static ExtentHtmlReporter htmlReporter;

	/** The feature test thread local. */
	private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();

	/** The scenario outline thread local. */
	private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();

	/** The scenario thread local. */
	static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();

	/** The step list thread local. */
	private static ThreadLocal<LinkedList<Step>> stepListThreadLocal = new InheritableThreadLocal<>();

	/** The step test thread local. */
	static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();

	/** The scenario outline flag. */
	private boolean scenarioOutlineFlag;

	/** The logger. */
	public final Logger logger = Logger.getLogger(getClass());

	/**
	 * Instantiates a new extent cucumber formatter.
	 *
	 * @author Deepak Rathod
	 * @param file the file
	 */
	public ExtentCucumberFormatter(File file) {
		setExtentHtmlReport(file);
		setExtentReport();
		stepListThreadLocal.set(new LinkedList<Step>());
		scenarioOutlineFlag = false;
	}

	/**
	 * Sets the extent html report.
	 *
	 * @author Deepak Rathod
	 * @param file
	 *            the new extent html report
	 */
	private static void setExtentHtmlReport(File file) {
		if (htmlReporter != null) {
			return;
		}
		if (file == null || file.getPath().isEmpty()) {
			file = new File(ExtentProperties.INSTANCE.getReportPath());
		}
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		htmlReporter = new ExtentHtmlReporter(file);
	}

	/**
	 * Gets the extent html report.
	 *@author GS-1629
	 * @return the extent html report
	 */
	static ExtentHtmlReporter getExtentHtmlReport() {
		return htmlReporter;
	}

	/**
	 * Sets the extent report. Set ExtentXServerUrl, project Name into HTML
	 * generated report.
	 *
	 * @author Deepak Rathod
	 */
	private static void setExtentReport() {
		if (extentReports != null) {
			return;
		}
		extentReports = new ExtentReports();
		ExtentProperties extentProperties = ExtentProperties.INSTANCE;
		if (extentProperties.getExtentXServerUrl() != null) {
			String extentXServerUrl = extentProperties.getExtentXServerUrl();
			try {
				URL url = new URL(extentXServerUrl);
				ExtentXReporter xReporter = new ExtentXReporter(url.getHost());
				xReporter.config().setServerUrl(extentXServerUrl);
				xReporter.config().setProjectName(extentProperties.getProjectName());
				extentReports.attachReporter(htmlReporter, xReporter);
				return;
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException("Invalid ExtentX Server URL", e);
			}
		}
		extentReports.attachReporter(htmlReporter);
	}

	/**
	 * Gets the extent report.
	 *@author GS-1629
	 * @return the extent report
	 */
	static ExtentReports getExtentReport() {
		return extentReports;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#syntaxError(java.lang.String,
	 * java.lang.String, java.util.List, java.lang.String, java.lang.Integer)
	 */
	@Override
	public void syntaxError(String state, String event, List<String> legalEvents, String uri, Integer line) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#uri(java.lang.String)
	 */
	@Override
	public void uri(String uri) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#feature(gherkin.formatter.model.Feature)
	 * Reporting feature details into extent report.
	 *
	 * @authod: Deepak Rathod
	 */
	@Override
	public void feature(Feature feature) {
		featureTestThreadLocal.set(getExtentReport()
				.createTest(com.aventstack.extentreports.gherkin.model.Feature.class, feature.getName()));
		ExtentTest test = featureTestThreadLocal.get();

		for (Tag tag : feature.getTags()) {
			test.assignCategory(tag.getName());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#scenarioOutline(gherkin.formatter.model.
	 * ScenarioOutline) This method is for print a log of scenarioOutline steps
	 *
	 * @authod : Deepak Rathod
	 */
	@Override
	public void scenarioOutline(ScenarioOutline scenarioOutline) {
		scenarioOutlineFlag = true;
		ExtentTest node = featureTestThreadLocal.get().createNode(
				com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioOutline.getName());
		scenarioOutlineThreadLocal.set(node);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gherkin.formatter.Formatter#examples(gherkin.formatter.model.Examples)
	 */
	@Override
	public void examples(Examples examples) {
		ExtentTest test = scenarioOutlineThreadLocal.get();

		String[][] data = null;
		List<ExamplesTableRow> rows = examples.getRows();
		int rowSize = rows.size();
		for (int i = 0; i < rowSize; i++) {
			ExamplesTableRow examplesTableRow = rows.get(i);
			List<String> cells = examplesTableRow.getCells();
			int cellSize = cells.size();
			if (data == null) {
				data = new String[rowSize][cellSize];
			}
			for (int j = 0; j < cellSize; j++) {
				data[i][j] = cells.get(j);
			}
		}
		test.info(MarkupHelper.createTable(data));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gherkin.formatter.Formatter#startOfScenarioLifeCycle(gherkin.formatter.
	 * model.Scenario)
	 */
	@Override
	public void startOfScenarioLifeCycle(Scenario scenario) {
		if (scenarioOutlineFlag) {
			scenarioOutlineFlag = false;
		}

		ExtentTest scenarioNode;
		if (scenarioOutlineThreadLocal.get() != null
				&& scenario.getKeyword().trim().equalsIgnoreCase("Scenario Outline")) {
			scenarioNode = scenarioOutlineThreadLocal.get()
					.createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
		} else {
			scenarioNode = featureTestThreadLocal.get()
					.createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
		}

		for (Tag tag : scenario.getTags()) {
			scenarioNode.assignCategory(tag.getName());
		}
		scenarioThreadLocal.set(scenarioNode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#background(gherkin.formatter.model.
	 * Background)
	 */
	@Override
	public void background(Background background) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gherkin.formatter.Formatter#scenario(gherkin.formatter.model.Scenario)
	 */
	@Override
	public void scenario(Scenario scenario) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#step(gherkin.formatter.model.Step)
	 */
	@Override
	public void step(Step step) {
		if (scenarioOutlineFlag) {
			return;
		}
		stepListThreadLocal.get().add(step);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * gherkin.formatter.Formatter#endOfScenarioLifeCycle(gherkin.formatter.
	 * model.Scenario)
	 */
	@Override
	public void endOfScenarioLifeCycle(Scenario scenario) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#done()
	 */
	@Override
	public void done() {
		getExtentReport().flush();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#close()
	 */
	@Override
	public void close() {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Formatter#eof()
	 */
	@Override
	public void eof() {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Reporter#before(gherkin.formatter.model.Match,
	 * gherkin.formatter.model.Result)
	 */
	@Override
	public void before(Match match, Result result) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Reporter#result(gherkin.formatter.model.Result)
	 */
	@Override
	public void result(Result result) {
		if (scenarioOutlineFlag) {
			return;
		}

		if (Result.PASSED.equals(result.getStatus())) {
			stepTestThreadLocal.get().pass(Result.PASSED);
		} else if (Result.FAILED.equals(result.getStatus())) {
			stepTestThreadLocal.get().fail(result.getError());
		} else if (Result.SKIPPED.equals(result)) {
			stepTestThreadLocal.get().skip(Result.SKIPPED.getStatus());
		} else if (Result.UNDEFINED.equals(result)) {
			stepTestThreadLocal.get().skip(Result.UNDEFINED.getStatus());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Reporter#after(gherkin.formatter.model.Match,
	 * gherkin.formatter.model.Result)
	 */
	@Override
	public void after(Match match, Result result) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see gherkin.formatter.Reporter#match(gherkin.formatter.model.Match)
	 */
	@Override
	public void match(Match match) {
		Step step = stepListThreadLocal.get().poll();
		String[][] data = null;
		if (step.getRows() != null) {
			List<DataTableRow> rows = step.getRows();
			int rowSize = rows.size();
			for (int i = 0; i < rowSize; i++) {
				DataTableRow dataTableRow = rows.get(i);
				List<String> cells = dataTableRow.getCells();
				int cellSize = cells.size();
				if (data == null) {
					data = new String[rowSize][cellSize];
				}
				for (int j = 0; j < cellSize; j++) {
					data[i][j] = cells.get(j);
				}
			}
		}

		ExtentTest scenarioTest = scenarioThreadLocal.get();
		ExtentTest stepTest = null;

		try {
			stepTest = scenarioTest.createNode(new GherkinKeyword(step.getKeyword()),
					step.getKeyword() + step.getName());
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage());
		}

		if (data != null && stepTest!=null) {
			Markup table = MarkupHelper.createTable(data);
			stepTest.info(table);
		}

		stepTestThreadLocal.set(stepTest);
	}

	/* (non-Javadoc)
	 * @see gherkin.formatter.Reporter#embedding(java.lang.String, byte[])
	 */
	@Override
	public void embedding(String mimeType, byte[] data) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}

	/* (non-Javadoc)
	 * @see gherkin.formatter.Reporter#write(java.lang.String)
	 */
	@Override
	public void write(String text) {
		// Do nothing because of no longer is required in as of now development
		// It might help in future scope
	}
}
