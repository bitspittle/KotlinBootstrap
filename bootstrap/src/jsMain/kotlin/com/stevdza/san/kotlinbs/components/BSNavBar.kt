package com.stevdza.san.kotlinbs.components

import androidx.compose.runtime.Composable
import com.stevdza.san.kotlinbs.forms.BSInput
import com.stevdza.san.kotlinbs.models.BackgroundStyle
import com.stevdza.san.kotlinbs.models.navbar.*
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.text.SpanText
import org.jetbrains.compose.web.css.CSSNumeric
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.*

/**
 * UI element that helps create responsive navigation menus for websites or web applications.
 * The NavBar provides a consistent and user-friendly way to navigate between different
 * sections or pages of a website.
 *
 * The NavBar typically appears at the top of the web page and contains various navigation
 * elements such as links, buttons, dropdown menus, and branding elements like logos or
 * site names. It adapts to different screen sizes and devices, making it ideal for
 * responsive web design.
 * @param stickyTop Whether to make this component stickied on the top.
 * @param brand A [NavBarBrand] that allows you to specify a brand text, and optional
 * brand image as well.
 * @param items Currently there are two different [NavItem]'s that you can use. [NavLink]
 * that represents a simple link within this component, usually used to navigate between
 * different pages on your website. And the second one [NavDropdown] used to display a
 * dropdown menu item within this component.
 * @param itemsAlignment The alignment of the NavBar items.
 * @param inputField Use this parameter if you want to display an input field.
 * @param button An optional button.
 * @param expand This parameter allows you to specify when should your component
 * display a collapsed state.
 * @param horizontalPadding The padding on the left/right of the component.
 * @param backgroundStyle A background style of this component.
 * */
@Composable
fun BSNavBar(
    modifier: Modifier = Modifier,
    stickyTop: Boolean = false,
    brand: NavBarBrand? = null,
    items: List<NavItem>,
    itemsAlignment: Alignment.Horizontal = Alignment.Start,
    inputField: NavBarInputField? = null,
    button: NavBarButton? = null,
    expand: NavBarExpand = NavBarExpand.LG,
    horizontalPadding: CSSNumeric = 8.px,
    backgroundStyle: BackgroundStyle = BackgroundStyle.Light
) {
    Nav(
        attrs = modifier
            .classNames(
                "navbar",
                expand.value,
                backgroundStyle.value
            )
            .thenIf(
                condition = stickyTop,
                other = Modifier.classNames("sticky-top")
            )
            .toAttrs {
                if (backgroundStyle == BackgroundStyle.Light ||
                    backgroundStyle == BackgroundStyle.Info ||
                    backgroundStyle == BackgroundStyle.Warning
                ) {
                    attr("data-bs-theme", "light")
                } else {
                    attr("data-bs-theme", "dark")
                }
            }
    ) {
        Div(
            attrs = Modifier
                .padding(leftRight = horizontalPadding)
                .classNames("container-fluid").toAttrs()
        ) {
            if (brand != null) {
                A(
                    attrs = Modifier
                        .classNames("navbar-brand")
                        .toAttrs(),
                    href = brand.href
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (brand.image != null) {
                            Image(
                                modifier = Modifier
                                    .width(brand.imageWidth)
                                    .margin(right = 8.px)
                                    .classNames("d-inline-block", "align-text-top")
                                    .attrsModifier {
                                        attr("alt", "logo")
                                    },
                                src = brand.image,
                                desc = "Brand Logo"
                            )
                        }
                        SpanText(brand.title)
                    }
                }
            }
            Button(
                attrs = Modifier
                    .classNames("navbar-toggler")
                    .toAttrs {
                        attr("data-bs-toggle", "collapse")
                        attr("data-bs-target", "#navbarSupportedContent")
                        attr("aria-controls", "navbarSupportedContent")
                        attr("aria-expanded", "false")
                        attr("aria-label", "Toggle Navigation")
                    }
            ) {
                Span(attrs = Modifier.classNames("navbar-toggler-icon").toAttrs())
            }
            Div(
                attrs = Modifier
                    .id("navbarSupportedContent")
                    .classNames("collapse", "navbar-collapse")
                    .toAttrs()
            ) {
                Ul(
                    attrs = Modifier
                        .classNames(
                            "navbar-nav",
                            if (itemsAlignment is Alignment.Start) "me-auto" else if (itemsAlignment is Alignment.End) "ms-auto" else "mx-auto"
                        )
                        .toAttrs()
                ) {
                    items.forEachIndexed { index, navItem ->
                        if (navItem is NavDropdown) {
                            Li(
                                attrs = Modifier
                                    .classNames("nav-item", "dropdown")
                                    .toAttrs()
                            ) {
                                A(
                                    attrs = Modifier
                                        .classNames("nav-link", "dropdown-toggle")
                                        .toAttrs {
                                            attr("role", "button")
                                            attr("data-bs-toggle", "dropdown")
                                            attr("aria-expanded", "false")
                                        }
                                ) {
                                    Text(value = navItem.placeholder)
                                }
                                Ul(
                                    attrs = Modifier.classNames("dropdown-menu").toAttrs()
                                ) {
                                    navItem.items.forEachIndexed { index, dropdownItem ->
                                        Li(
                                            attrs = Modifier
                                                .id(dropdownItem.id)
                                                .onClick {
                                                    dropdownItem.onClick(index)
                                                }
                                                .toAttrs()
                                        ) {
                                            A(
                                                attrs = Modifier
                                                    .classNames("dropdown-item")
                                                    .cursor(Cursor.Pointer)
                                                    .toAttrs(),
                                            ) { Text(value = dropdownItem.title) }
                                        }
                                    }
                                }
                            }
                        } else if (navItem is NavLink) {
                            Li(
                                attrs = Modifier
                                    .id(navItem.id)
                                    .classNames("nav-item")
                                    .onClick { navItem.onClick(index) }
                                    .toAttrs()
                            ) {
                                A(
                                    attrs = Modifier
                                        .classNames("nav-link")
                                        .thenIf(
                                            condition = navItem.active,
                                            other = Modifier.classNames("active")
                                        )
                                        .thenIf(
                                            condition = navItem.disabled,
                                            other = Modifier.classNames("disabled")
                                        )
                                        .cursor(Cursor.Pointer)
                                        .toAttrs {
                                            if (navItem.active) attr("aria-current", "page")
                                        }
                                ) {
                                    Text(value = navItem.title)
                                }
                            }
                        }
                    }
                }
                Form(
                    attrs = Modifier
                        .classNames("d-flex")
                        .toAttrs {
                            attr("role", "search")
                        }
                ) {
                    if (inputField != null) {
                        BSInput(
                            modifier = Modifier
                                .thenIf(
                                    condition = button != null,
                                    other = Modifier.margin(right = 8.px)
                                ),
                            id = inputField.id,
                            placeholder = inputField.placeholder,
                            value = inputField.value,
                            onValueChange = inputField.onValueChange,
                            onEnterClick = inputField.onEnterClick
                        )
                    }
                    if (button != null) {
                        BSButton(
                            id = button.id,
                            text = button.text,
                            variant = button.variant,
                            disabled = button.disabled,
                            loading = button.loading,
                            loadingText = button.loadingText,
                            badge = button.badge,
                            onClick = button.onClick
                        )
                    }
                }
            }
        }
    }
}