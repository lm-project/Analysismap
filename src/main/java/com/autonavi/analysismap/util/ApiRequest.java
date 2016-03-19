package com.autonavi.analysismap.util;

import com.autonavi.analysismap.entity.ResponseStatus;

public interface ApiRequest {
	ResponseStatus select(String key);
}
