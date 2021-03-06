package es.oaw.wcagem;

import static es.inteco.common.Constants.CRAWLER_PROPERTIES;

import java.io.File;
import java.util.List;

import org.jopendocument.dom.ODValueType;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import es.inteco.common.properties.PropertiesManager;

/**
 * The Class WcagOdsUtils.
 * 
 * Generates an ods
 * 
 */
public final class WcagOdsUtils {
	/** The Constant MAX_PAGES. */
	private static final int MAX_PAGES = 30;
	/** The Constant EARL_INAPPLICABLE. */
	private static final String EARL_INAPPLICABLE = "earl:inapplicable";
	/** The Constant EARL_FAILED. */
	private static final String EARL_FAILED = "earl:failed";
	/** The Constant EARL_PASSED. */
	private static final String EARL_PASSED = "earl:passed";
	/** The Constant EARL_CANNOT_TELL. */
	private static final String EARL_CANNOT_TELL = "earl:cantTell";

	/**
	 * Generate ods.
	 *
	 * @param report the report
	 * @return the spread sheet
	 * @throws Exception the exception
	 */
	public static SpreadSheet generateOds(final WcagEmReport report) throws Exception {
		final PropertiesManager pmgr = new PropertiesManager();
		File inputFile = new File(pmgr.getValue(CRAWLER_PROPERTIES, "export.ods.template"));
		// Load template
		final SpreadSheet workbook = SpreadSheet.createFromFile(inputFile);
		final Sheet sheet = workbook.getSheet("03.Muestra");
		int resultsProcessed = 0;
		int initRow = 8; // Initial rowcount
		final List<Webpage_> webpageList = report.getGraph().get(0).getRandomSample().getWebpage();
		final int totalPages = webpageList.size();
		fillNotTell(workbook, totalPages < MAX_PAGES ? totalPages : MAX_PAGES);
		for (Webpage_ webpage : webpageList) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				sheet.getCellAt("C" + initRow).setValue(webpage.getTitle(), ODValueType.STRING, true, false);
				sheet.getCellAt("D" + initRow).setValue("Aleatoria", ODValueType.STRING, true, false);
				sheet.getCellAt("E" + initRow).setValue(webpage.getSource(), ODValueType.STRING, true, false);
				initRow++;
			}
		}
		final Sheet sheetP1 = workbook.getSheet("P1.Perceptible");
		final Sheet sheetP2 = workbook.getSheet("P2.Operable");
		final Sheet sheetP3 = workbook.getSheet("P3.Comprensible");
		final Sheet sheetP4 = workbook.getSheet("P4.Robusto");
		resultsProcessed = 0;
		for (AuditResult auditResult : report.getGraph().get(0).getAuditResult()) {
			if (resultsProcessed < MAX_PAGES) {
				resultsProcessed++;
				switch (auditResult.getTest()) {
				// P1
				case "WCAG2:non-text-content":
					fillResult(sheetP1, auditResult, 19);
					break;
				case "WCAG2:info-and-relationships":
					fillResult(sheetP1, auditResult, 217);
					break;
				case "WCAG2:orientation":
					fillResult(sheetP1, auditResult, 316);
					break;
				case "WCAG2:identify-input-purpose":
					fillResult(sheetP1, auditResult, 349);
					break;
				case "WCAG2:contrast-minimum":
					fillResult(sheetP1, auditResult, 448);
					break;
				case "WCAG2:reflow":
					fillResult(sheetP1, auditResult, 548);
					break;
				case "WCAG2:text-spacing":
					fillResult(sheetP1, auditResult, 613);
					break;
				// P2
				case "WCAG2:keyboard":
					fillResult(sheetP2, auditResult, 19);
					break;
				case "WCAG2:timing-adjustable":
					fillResult(sheetP2, auditResult, 119);
					break;
				case "WCAG2:tpause-stop-hide":
					fillResult(sheetP2, auditResult, 151);
					break;
				case "WCAG2:three-flashes-or-below-threshold":
					fillResult(sheetP2, auditResult, 184);
					break;
				case "WCAG2:bypass-blocks":
					fillResult(sheetP2, auditResult, 217);
					break;
				case "WCAG2:page-titled":
					fillResult(sheetP2, auditResult, 250);
					break;
				case "WCAG2:focus-order":
					fillResult(sheetP2, auditResult, 283);
					break;
				case "WCAG2:link-purpose-in-context":
					fillResult(sheetP2, auditResult, 316);
					break;
				case "WCAG2:multiple-ways":
					fillResult(sheetP2, auditResult, 349);
					break;
				case "WCAG2:focus-visible":
					fillResult(sheetP2, auditResult, 416);
					break;
				case "WCAG2:label-in-name":
					fillResult(sheetP2, auditResult, 514);
					break;
				// P3
				case "WCAG2:language-of-page":
					fillResult(sheetP3, auditResult, 19);
					break;
				case "WCAG2:language-of-parts":
					fillResult(sheetP3, auditResult, 52);
					break;
				case "WCAG2:on-focus":
					fillResult(sheetP3, auditResult, 85);
					break;
				case "WCAG2:on-input":
					fillResult(sheetP3, auditResult, 119);
					break;
				case "WCAG2:consistent-navigation":
					fillResult(sheetP3, auditResult, 151);
					break;
				case "WCAG2:labels-or-instructions":
					fillResult(sheetP3, auditResult, 217);
					break;
				// p4
				case "WCAG2:name-role-value":
					fillResult(sheetP4, auditResult, 52);
					break;
				default:
					break;
				}
			}
		}
		// Save
		// File outputFile = new File("/home/alvaro/Downloads/Borrador_Informe_Revision_Profunidad_v1_M.ods");
		// workbook.saveAs(outputFile);
		return workbook;
	}

	/**
	 * Extracted.
	 *
	 * @param sheet        the sheet
	 * @param auditResult  the audit result
	 * @param initRowValue the init row value
	 */
	private static void fillResult(final Sheet sheet, AuditResult auditResult, final int initRowValue) {
		int initRow = initRowValue;
		int resultsProcessed = 0;
		for (HasPart hasPart : auditResult.getHasPart()) {
			if (resultsProcessed < MAX_PAGES) {
				final MutableCell<SpreadSheet> cellAt = sheet.getCellAt("D" + initRow);
				cellAt.clearValue();
				cellAt.setValue(odsOutcome(hasPart.getResult().getOutcome()));
				resultsProcessed++;
				initRow++;
			}
		}
	}

	/**
	 * Fill not tell.
	 *
	 * @param workbook   the workbook
	 * @param totalPages the total pages
	 */
	private static void fillNotTell(final SpreadSheet workbook, final int totalPages) {
		final Sheet sheetP1 = workbook.getSheet("P1.Perceptible");
		int tableRowIndex = 19;
		while (tableRowIndex <= 613) {
			for (int i = 0; i < totalPages; i++) {
				sheetP1.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 33;
		}
		final Sheet sheetP2 = workbook.getSheet("P2.Operable");
		tableRowIndex = 19;
		while (tableRowIndex <= 547) {
			for (int i = 0; i < totalPages; i++) {
				sheetP2.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 33;
		}
		final Sheet sheetP3 = workbook.getSheet("P3.Comprensible");
		tableRowIndex = 19;
		while (tableRowIndex <= 316) {
			for (int i = 0; i < totalPages; i++) {
				sheetP3.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 33;
		}
		final Sheet sheetP4 = workbook.getSheet("P4.Robusto");
		tableRowIndex = 19;
		while (tableRowIndex <= 85) {
			for (int i = 0; i < totalPages; i++) {
				sheetP4.getCellAt("D" + (i + tableRowIndex)).setValue("N/T");
			}
			tableRowIndex = tableRowIndex + 33;
		}
	}

	/**
	 * Ods outcome.
	 *
	 * @param jsonOutcome the json outcome
	 * @return the string
	 */
	private static String odsOutcome(final String jsonOutcome) {
		switch (jsonOutcome) {
		case EARL_PASSED:
			return "N/D";
		case EARL_FAILED:
			return "Falla";
		case EARL_CANNOT_TELL:
			return "N/T";
		case EARL_INAPPLICABLE:
			return "N/A";
		default:
			return "N/T";
		}
	}
}
