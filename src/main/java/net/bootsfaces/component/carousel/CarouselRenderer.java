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

package net.bootsfaces.component.carousel;

import java.io.IOException;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import net.bootsfaces.component.ajax.AJAXRenderer;
import net.bootsfaces.component.carouselControl.CarouselControl;
import net.bootsfaces.component.carouselControl.CarouselControlRenderer;
import net.bootsfaces.component.carouselItem.CarouselItem;
import net.bootsfaces.render.CoreRenderer;
import net.bootsfaces.render.Responsive;
import net.bootsfaces.render.Tooltip;

/** This class generates the HTML code of &lt;b:carousel /&gt;. */
@FacesRenderer(componentFamily = "net.bootsfaces.component", rendererType = "net.bootsfaces.component.carousel.Carousel")
public class CarouselRenderer extends CoreRenderer {
	/**
	 * This methods receives and processes input made by the user. More
	 * specifically, it ckecks whether the user has interacted with the current
	 * b:carousel. The default implementation simply stores the input value in
	 * the list of submitted values. If the validation checks are passed, the
	 * values in the <code>submittedValues</code> list are store in the backend
	 * bean.
	 *
	 * @param context
	 *            the FacesContext.
	 * @param component
	 *            the current b:carousel.
	 */
	@Override
	public void decode(FacesContext context, UIComponent component) {
		String clientId = escapeClientId(component.getClientId());

		new AJAXRenderer().decode(context, component, clientId);
	}

	/**
	 * This methods generates the HTML code of the current b:carousel.
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
	 *            the current b:carousel.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}
		Carousel carousel = (Carousel) component;
		ResponseWriter rw = context.getResponseWriter();
		String clientId = escapeClientId(component.getClientId());

		rw.startElement("div", carousel);
		Tooltip.generateTooltip(context, carousel, rw);

		rw.writeAttribute("id", clientId, "id");
		Tooltip.generateTooltip(context, carousel, rw);
		AJAXRenderer.generateBootsFacesAJAXAndJavaScript(context, carousel, rw, false);

		writeAttribute(rw, "data-interval", carousel.getInterval());
		writeAttribute(rw, "data-pause", carousel.getPause());
		writeAttribute(rw, "data-wrap", String.valueOf(carousel.isWrap()));
		if (carousel.isStartAnimation()) {
			rw.writeAttribute("data-ride", "carousel", "data-ride");
		}
		rw.writeAttribute("style", carousel.getStyle(), "style");
		String styleClass = carousel.getStyleClass();
		if (null == styleClass)
			styleClass = "carousel " + (carousel.isSlide() ? "slide " : "");
		else
			styleClass = "carousel " + (carousel.isSlide() ? "slide " : "") + styleClass;
		styleClass += Responsive.getResponsiveStyleClass(carousel, false);

		rw.writeAttribute("class", styleClass, "class");

		int activeIndex = carousel.getActiveIndex();
		int currentIndex = 0;
		List<UIComponent> children = carousel.getChildren();
		for (UIComponent c : children) {
			if (c instanceof CarouselItem) {
				((CarouselItem) c).setActive(currentIndex == activeIndex);
				currentIndex++;
			}
		}

		if (!carousel.isDisabled()) {
			if (carousel.isShowIndicators()) {
				rw.startElement("ol", component);
				rw.writeAttribute("class", "carousel-indicators", "class");
				currentIndex = 0;
				for (UIComponent c : children) {
					if (c instanceof CarouselItem) {
						rw.startElement("li", c);
						rw.writeAttribute("data-target", "#" + clientId, "data-target");
						rw.writeAttribute("data-slide-to", String.valueOf(currentIndex), "data-slide-to");
						if ((currentIndex == activeIndex)) {
							rw.writeAttribute("class", "active", "class");
						}
						rw.endElement("li");
						currentIndex++;
					}
				}
				rw.endElement("ol");
			}
		}

		rw.startElement("div", carousel);
		rw.writeAttribute("class", "carousel-inner", "class");
		rw.writeAttribute("role", "listbox", "role");
	}

	/**
	 * This methods generates the HTML code of the current b:carousel.
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
	 *            the current b:carousel.
	 * @throws IOException
	 *             thrown if something goes wrong when writing the HTML code.
	 */
	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		if (!component.isRendered()) {
			return;
		}

		Carousel carousel = (Carousel) component;
		ResponseWriter rw = context.getResponseWriter();
		rw.endElement("div");
		String clientId = super.escapeClientId(component.getClientId());

		if (!carousel.isDisabled()) {
			if (carousel.isShowControls()) {
				CarouselControlRenderer ccr = new CarouselControlRenderer();
				boolean foundCustomControl = false;
				List<UIComponent> children = carousel.getChildren();
				for (UIComponent c : children) {
					if (c instanceof CarouselControl) {
						foundCustomControl = true;
						ccr.myEncodeBegin(context, c);
						ccr.myEncodeEnd(context, c);
					}
				}
				if (!foundCustomControl) {
					ccr.encodeDefaultControls(context, component, clientId);
				}
			}
		}

		rw.endElement("div");
		new AJAXRenderer().generateBootsFacesAJAXAndJavaScriptForJQuery(context, component, rw, "#"+clientId, null);

		Tooltip.activateTooltips(context, carousel);
	}
}
