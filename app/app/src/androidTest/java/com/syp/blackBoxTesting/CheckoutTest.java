package com.syp.blackBoxTesting;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import com.syp.MainActivity;
import com.syp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CheckoutTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.ACCESS_FINE_LOCATION");

    @Test
    public void checkoutTest() {
        ViewInteraction appCompatButton = onView(
                Matchers.allOf(ViewMatchers.withId(R.id.signInButton), withText("login with google"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.TESTinvisibleRedMarker_PotOfChang),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton6.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.view_cafe_button), isDisplayed()));
        appCompatButton2.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction view = onView(
                allOf(withId(R.id.userItemRow),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                0),
                        isDisplayed()));
        view.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_btn), withText("+"),
                        childAtPosition(
                                allOf(withId(R.id.layout),
                                        childAtPosition(
                                                withId(R.id.orderItemCountStepper),
                                                0)),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.orderItemAddToCart), withText("Add to Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Navigate up"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatImageButton2 = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton2.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        4),
                        isDisplayed()));
        navigationMenuItemView.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.confirmBtn), withText("Complete Checkout"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.nav_host_fragment),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton5.perform(click());
        try {
            Thread.sleep(7500);
        } catch (InterruptedException ie) {

        }
        ViewInteraction viewMap = onView(
                allOf(withContentDescription("Google Map"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.map),
                                        0),
                                0),
                        isDisplayed()));
        viewMap.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
