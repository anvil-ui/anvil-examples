package com.example.anvil.login;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static trikita.anvil.v15.Attrs.*;

import trikita.anvil.Anvil;
import trikita.anvil.RenderableView;

//
// Imagine a simple login form.
//
// - There are login/password input fields
// - There is a login button
// - There is an error message text label
// - There is a login progress indicator (just a rotating spinner)
//
// Now, the logic:
//
// - Login button must be disabled if login or password input fields are empty
// - Input fields and button are disabled if login is now in progress
// - Error message is shown if last login attempt has failed, but hidden if no
//   login has been performed yet, or if last login attempt succeeded
//
// Try implementing this in a traditional android way: updating view properties
// with setEnabled()/setVisible from inside your text watchers and listeners
// and background tasks. Your view state becomes scattered across the whole
// class and it's very easy to miss something.
//
// Here's Anvil way:
//
public class LoginView extends RenderableView {

	// Current input: login and password strings
	private String mLogin = "";
	private String mPassword = "";

	// Current flags: "has last login failed" and "is login in progress now"
	private boolean mLoginFailed = false;
	private boolean mIsLoggingIn = false; 

	public LoginView(Context c) {
		super(c);
	}

	// Rule: login button must be enabled if both inputs are non-empty
	private boolean isLoginAllowed() {
		return mLogin.length() > 0 && mPassword.length() > 0;
	}

	// Click listener for the login button
	private void onLoginClicked(View v) {
		mIsLoggingIn = true;
		performLogin(mLogin, mPassword);
	}

	// This should really call API service and must not be part of the View class
	// I use it here for demonstration only
	private void performLogin(final String login, final String password) {
		postDelayed(() -> {
			onLoginFinished(login.equals("admin") && password.equals("qwerty"));
		}, 1000);
	}

	// This might be a callback from the API service
	public void onLoginFinished(boolean success) {
		mIsLoggingIn = false;
		mLoginFailed = !success;
		if (success) {
			// Logged in successfully, show toast at least
			Toast
				.makeText(getContext(), R.string.login_successful, Toast.LENGTH_SHORT)
				.show();
		}
		// This callback happens in the background, so call Anvil.render explicitly
		Anvil.render();
	}

	// Now we only define the form layout and bind all the methods and fields
	// from the above.
	public ViewNode view() {
		return
			v(LinearLayout.class,
				size(FILL, WRAP),
				padding(dip(16)),
				orientation(LinearLayout.VERTICAL),

				// Login progress indicator, visible only when login is in progress
				v(ProgressBar.class,
					visibility(mIsLoggingIn)),

				// Error message, visible only when login is in progress
				v(TextView.class,
					textColor(Color.RED),
					text(R.string.error_label),
					visibility(mLoginFailed)),
				
				v(TextView.class,
					text(R.string.login_label)),
				v(EditText.class,
					size(FILL, WRAP),
					hint(R.string.login_placeholder),
					// Enabled when no login is in progress
					enabled(!mIsLoggingIn),
					// Update mLogin variablea as the user enters text
					onTextChanged(s -> mLogin = s)),

				v(TextView.class,
					text(R.string.password_label)),
				v(EditText.class,
					size(FILL, WRAP),
					hint(R.string.password_placeholder),
					// Enabled when no login is in progress
					enabled(!mIsLoggingIn),
					// Update mPassword variablea as the user enters text
					onTextChanged(s -> mPassword = s)),

				v(Button.class,
					size(FILL, WRAP),
					text(R.string.login_button),
					// Enabled when login is allowed and no current login is happening
					enabled(isLoginAllowed() && !mIsLoggingIn),
					onClick(this::onLoginClicked)));
	}
}
