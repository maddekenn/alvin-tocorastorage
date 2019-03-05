package se.uu.ub.cora.alvin.tocorastorage.fedora;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.httphandler.HttpMultiPartUploader;

public class HttpHandlerFactorySpy implements HttpHandlerFactory {
	public List<String> urls = new ArrayList<>();
	public List<HttpHandlerSpy> factoredHttpHandlers = new ArrayList<>();
	public String responseText = "";
	public int responseCode = 200;
	public List<String> responseTexts = new ArrayList<>();

	@Override
	public HttpHandler factor(String url) {
		urls.add(url);
		HttpHandlerSpy httpHandlerSpy = new HttpHandlerSpy();
		factoredHttpHandlers.add(httpHandlerSpy);

		int numOfHandlersFactored = factoredHttpHandlers.size();

		// httpHandlerSpy.responseText = responseText;
		httpHandlerSpy.responseText = responseTexts.get(numOfHandlersFactored - 1);
		httpHandlerSpy.responseCode = responseCode;
		return httpHandlerSpy;
	}

	@Override
	public HttpMultiPartUploader factorHttpMultiPartUploader(String url) {
		// TODO Auto-generated method stub
		return null;
	}

}
