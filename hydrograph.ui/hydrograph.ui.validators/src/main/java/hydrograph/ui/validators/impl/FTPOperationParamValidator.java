/*******************************************************************************
 * Copyright 2017 Capital One Services, LLC and Bitwise, Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package hydrograph.ui.validators.impl;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import hydrograph.ui.common.util.Constants;
import hydrograph.ui.common.util.ParameterUtil;
import hydrograph.ui.datastructure.property.FTPAuthOperationDetails;
import hydrograph.ui.datastructure.property.FixedWidthGridRow;

/**
 * The Class FTPOperationParamValidator to validate operations field
 * @author Bitwise
 *
 */
public class FTPOperationParamValidator implements IValidator{
	private String errorMessage;

	@Override
	public boolean validateMap(Object object, String propertyName,
			Map<String, List<FixedWidthGridRow>> inputSchemaMap) {
		Map<String, Object> propertyMap = (Map<String, Object>) object;
		if (propertyMap != null && !propertyMap.isEmpty()) {
			return validate(propertyMap.get(propertyName), propertyName, inputSchemaMap,false);
		}
		return false;
	}

	@Override
	public boolean validate(Object object, String propertyName, Map<String, List<FixedWidthGridRow>> inputSchemaMap,
			boolean isJobFileImported) {
		Map<String, FTPAuthOperationDetails> additionalParam = null;
		if(object != null && Map.class.isAssignableFrom(object.getClass())){
				additionalParam = (Map<String, FTPAuthOperationDetails>) object;
				if (!additionalParam.isEmpty()) {
					for(Map.Entry<String, FTPAuthOperationDetails> map : additionalParam.entrySet()){
						FTPAuthOperationDetails details = map.getValue();
						if(map.getKey().contains("S3")){
							if(details.getField1() == null || details.getField1().isEmpty()){
								errorMessage = propertyName + " can not be blank";
								return false;
							}
							if(details.getField2() == null || details.getField2().isEmpty()){
								errorMessage = propertyName + " can not be blank";
								return false;
							}
							if(details.getField3() == null || details.getField3().isEmpty()){
								errorMessage = propertyName + " can not be blank";
								return false;
							}
							if(details.getField4() == null || details.getField4().isEmpty()){
								errorMessage = propertyName + " can not be blank";
								return false;
							}
							
							/*if(validateText(details.getField2(), propertyName) && validateText(details.getField3(), propertyName)
									&& validateText(details.getField4(), propertyName)){
								return true;
							}else{
								errorMessage = propertyName + " is mandatory";
								return false;
							}*/
							
						}else {
							if(details.getField1() == null || details.getField1().isEmpty()){
								errorMessage = propertyName + " can not be blank";
								return false;
							}
							if(details.getField2() == null || details.getField2().isEmpty()){
								errorMessage = propertyName + " can not be blank";
								return false;
							}
						}
					}
				}
			}
		return true;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	private boolean validateText(String text, String propertyName){
		if (StringUtils.isNotBlank(text)) {
			Matcher matchs = Pattern.compile(Constants.REGEX).matcher(text);
			return validateNumericField(text, propertyName, errorMessage, matchs);
		}
		return false;
	}
	
	
	private boolean validateNumericField(String value, String propertyName, String errorMessage, Matcher matchs){
		boolean isValid = false;
		if(matchs.matches()||ParameterUtil.isParameter(value)){
			isValid =  true;
		}else{
			isValid = false;
			errorMessage = propertyName + " is mandatory";
		}
		return isValid;
	}
}