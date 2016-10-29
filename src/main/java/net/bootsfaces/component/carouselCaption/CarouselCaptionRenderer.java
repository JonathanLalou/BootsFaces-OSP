/**
 *  Copyright 2014-16 by Riccardo Massera (TheCoder4.Eu) and Stephan Rauh (http://www.beyondjava.net).
 *  
 *  This file is part of BootsFaces.
 *  
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
 */

package net.bootsfaces.component.carouselCaption;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import net.bootsfaces.component.ajax.AJAXRenderer;
import net.bootsfaces.render.CoreRenderer;
import net.bootsfaces.render.Tooltip;

/** This class generates the HTML code of &lt;b:carouselCaption /&gt;. */
@FacesRenderer(componentFamily = "net.bootsfaces.component", rendererType = "net.bootsfaces.component.carouselCaption.CarouselCaption")
public class CarouselCaptionRenderer extends CoreRenderer {
	/**
	 * This methods receives and processes input made by the user. More
	 * specifically, it ckecks whether the user has interacted with the current
	 * b:carouselCaption. The default implementation simply stores the input
	 * value in the list of submitted values. If the validation checks are
	 * passed, the values in the <code>submittedValues</code> list are store in
	 * the backend bean.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:carouselCaption.
	 */
	@Override
	public void decode(FacesContext context, UIComponent component) {
		new AJAXRenderer().decode(context, component);
	}

	/**
	 * This methods generates the HTML code of the current b:carouselCaption.
	 * <code>encodeBegin</code> generates the start of the component. After the,
	 * the JSF framework calls <code>encodeChildren()</code> to generate the
	 * HTML code between the beginning and the end of the component. For
	 * instance, in the case of a panel component the content of the panel is
	 * generated by <code>encodeChildren()</code>. After that,
	 * <code>encodeEnd()</code> is called to generate the rest of the HTML code.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:carouselCaption.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		ResponseWriter rw = context.getResponseWriter();

		rw.startElement("div", component);

		rw.writeAttribute("id", component.getId(), "id");

		if (component instanceof CarouselCaption) {
			Tooltip.generateTooltip(context, ((CarouselCaption) component), rw);
			AJAXRenderer.generateBootsFacesAJAXAndJavaScript(context, ((CarouselCaption) component), rw, false);
			rw.writeAttribute("style", ((CarouselCaption) component).getStyle(), "style");
		}
		String styleClass = null;
		if (component instanceof CarouselCaption) {
			styleClass = ((CarouselCaption) component).getStyleClass();
		}
		if (null == styleClass)
			styleClass = "carousel-caption";
		else
			styleClass = "carousel-caption " + styleClass;
		rw.writeAttribute("class", styleClass, "class");
	}

	/**
	 * This methods generates the HTML code of the current b:carouselCaption.
	 * <code>encodeBegin</code> generates the start of the component. After the,
	 * the JSF framework calls <code>encodeChildren()</code> to generate the
	 * HTML code between the beginning and the end of the component. For
	 * instance, in the case of a panel component the content of the panel is
	 * generated by <code>encodeChildren()</code>. After that,
	 * <code>encodeEnd()</code> is called to generate the rest of the HTML code.
	 * 
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:carouselCaption.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		ResponseWriter rw = context.getResponseWriter();
		rw.endElement("div");
		if (component instanceof CarouselCaption) {
			Tooltip.activateTooltips(context, component);
		}

	}

	public void encodeDefaultCaption(FacesContext context, UIComponent component, String caption) throws IOException {
		encodeBegin(context, component);
		ResponseWriter rw = context.getResponseWriter();
		rw.startElement("h3", component);
		rw.append(caption);
		rw.endElement("h3");
		encodeEnd(context, component);
	}

}
