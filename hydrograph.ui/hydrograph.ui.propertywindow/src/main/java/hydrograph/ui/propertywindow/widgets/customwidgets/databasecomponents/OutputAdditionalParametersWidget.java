package hydrograph.ui.propertywindow.widgets.customwidgets.databasecomponents;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.logging.factory.LogFactory;
import hydrograph.ui.propertywindow.factory.ListenerFactory;
import hydrograph.ui.propertywindow.property.ComponentConfigrationProperty;
import hydrograph.ui.propertywindow.property.ComponentMiscellaneousProperties;
import hydrograph.ui.propertywindow.property.Property;
import hydrograph.ui.propertywindow.propertydialog.PropertyDialogButtonBar;
import hydrograph.ui.propertywindow.widgets.customwidgets.AbstractWidget;
import hydrograph.ui.propertywindow.widgets.customwidgets.config.RuntimeConfig;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultButton;
import hydrograph.ui.propertywindow.widgets.gridwidgets.basic.ELTDefaultLable;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.AbstractELTContainerWidget;
import hydrograph.ui.propertywindow.widgets.gridwidgets.container.ELTDefaultSubgroupComposite;
import hydrograph.ui.propertywindow.widgets.listeners.ListenerHelper;

public class OutputAdditionalParametersWidget extends AbstractWidget {

	private static final Logger logger = LogFactory.INSTANCE.getLogger(OutputAdditionalParametersWidget.class);
	private String propertyName;
	private Map<String, Object> initialMap;
	private RuntimeConfig runtimeConfig;
	private Shell shell;
	private ArrayList<AbstractWidget> widgets;
	private LinkedHashMap<String, Object> tempPropertyMap;

	/**
	 * Instantiates a new OutputAdditionalParametersWidget widget.
	 * 
	 * @param componentConfigProp
	 *            the component configuration property
	 * @param componentMiscProps
	 *            the component miscellaneous properties
	 * @param propDialogButtonBar
	 *            the property dialog button bar
	 */
	public OutputAdditionalParametersWidget(ComponentConfigrationProperty componentConfigProp,
			ComponentMiscellaneousProperties componentMiscProps, PropertyDialogButtonBar propDialogButtonBar) {
		super(componentConfigProp, componentMiscProps, propDialogButtonBar);

		this.propertyName = componentConfigProp.getPropertyName();
		if (componentConfigProp.getPropertyValue() != null
				&& (Map.class).isAssignableFrom(componentConfigProp.getPropertyValue().getClass())) {
			this.initialMap = (Map<String, Object>) componentConfigProp.getPropertyValue();
		}
		if (initialMap == null) {
			this.initialMap = new LinkedHashMap<String, Object>();
		}
	}

	@Override
	public void attachToPropertySubGroup(AbstractELTContainerWidget subGroup) {
		ELTDefaultSubgroupComposite outputAdditionalParameterComposite = new ELTDefaultSubgroupComposite(
				subGroup.getContainerControl());
		outputAdditionalParameterComposite.createContainerWidget();
		this.shell = outputAdditionalParameterComposite.getContainerControl().getShell();
		this.runtimeConfig = (RuntimeConfig) widgetConfig;

		ELTDefaultLable outputAdditionalParameterLabel = new ELTDefaultLable(runtimeConfig.getLabel());
		outputAdditionalParameterComposite.attachWidget(outputAdditionalParameterLabel);
		setPropertyHelpWidget((Control) outputAdditionalParameterLabel.getSWTWidgetControl());

		ELTDefaultButton outputAdditionalParameterButton = new ELTDefaultButton(Constants.EDIT);

		outputAdditionalParameterComposite.attachWidget(outputAdditionalParameterButton);

		try {
			outputAdditionalParameterButton.attachListener(ListenerFactory.Listners.RUNTIME_BUTTON_CLICK.getListener(),
					propertyDialogButtonBar, new ListenerHelper(this.getClass().getName(), this),
					outputAdditionalParameterButton.getSWTWidgetControl());

		} catch (Exception exception) {
			logger.error("Error occured while attaching listener to Runtime Properties window", exception);
		}

	}

	@Override
	public LinkedHashMap<String, Object> getProperties() {
		tempPropertyMap = new LinkedHashMap<>();
		tempPropertyMap.put(this.propertyName, this.initialMap);
		setToolTipErrorMessage();
		return tempPropertyMap;
	}

	@Override
	public boolean isWidgetValid() {
		return validateAgainstValidationRule(initialMap);
	}

	@Override
	public void addModifyListener(Property property, ArrayList<AbstractWidget> widgetList) {
		this.widgets = widgetList;
	}

	/**
	 * Set the tool tip error message
	 */
	protected void setToolTipErrorMessage() {
		String toolTipErrorMessage = null;
		setToolTipMessage(toolTipErrorMessage);
	}

	public void newWindowLauncher() {
		initialMap = new LinkedHashMap<>(initialMap);
		OutputAdditionalParametersDialog outputAdditionalParametersDialog = new OutputAdditionalParametersDialog(shell,
				runtimeConfig.getWindowLabel(), propertyDialogButtonBar, initialMap);
		outputAdditionalParametersDialog.open();
		initialMap = outputAdditionalParametersDialog.getOutputAdditionalParameterValues();
		showHideErrorSymbol(widgets);
		propertyDialogButtonBar.enableApplyButton(true);

	}

}