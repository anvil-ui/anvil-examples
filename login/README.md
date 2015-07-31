# Login form

Login form is normally just two edit texts and a button.

However, button should be disabled when input is invalid, inputs and button
should be disabled when login is in progress etc etc.

<img alt="screenshot" src="./screenshot.png" width="320">

With Anvil you only need to define the rules and all views will be in sync:

``` java
private String mLogin = "";
private String mPassword = "";
private boolean mLoginFailed = false;
private boolean mIsLoggingIn = false; 

...

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
	
	// Login input
	v(EditText.class,
		size(FILL, WRAP),
		hint(R.string.login_placeholder),
		// Enabled when no login is in progress
		enabled(!mIsLoggingIn),
		// Update mLogin variable as the user enters text
		onTextChanged(s -> mLogin = s)),

	// Password input
	v(EditText.class,
		size(FILL, WRAP),
		hint(R.string.password_placeholder),
		// Enabled when no login is in progress
		enabled(!mIsLoggingIn),
		// Update mPassword variable as the user enters text
		onTextChanged(s -> mPassword = s)),

	// Login button
	v(Button.class,
		size(FILL, WRAP),
		text(R.string.login_button),
		// Enabled when login is allowed and no current login is happening
		enabled(isLoginAllowed() && !mIsLoggingIn),
		// onLoginClicked(View) method start the login process
		onClick(this::onLoginClicked)));
```
