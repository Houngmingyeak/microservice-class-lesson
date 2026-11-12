@param String title = "Create an account"
@param String locale = "en"
@param String error = null
@param String csrfParameterName = "_csrf"
@param String csrfToken = ""

<!DOCTYPE html>
<html lang="${locale}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - ISTAD IAM</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            min-height: 100vh;
            display: flex; align-items: center; justify-content: center;
            font-family: "Segoe UI", system-ui, -apple-system, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 20px;
        }
        .card {
            width: 100%; max-width: 460px;
            background: #fff; border-radius: 16px;
            padding: 36px; box-shadow: 0 20px 50px rgba(0, 0, 0, 0.25);
        }
        .title { text-align: center; font-size: 26px; font-weight: 700; color: #667eea; margin-bottom: 6px; }
        .subtitle { text-align: center; color: #777; font-size: 14px; margin-bottom: 24px; }
        .alert-error { background: #fdecea; color: #b3261e; border: 1px solid #f5c2be; padding: 12px; border-radius: 8px; font-size: 14px; margin-bottom: 16px; }
        .field { margin-bottom: 14px; }
        label { display: block; font-size: 13px; font-weight: 600; color: #333; margin-bottom: 4px; }
        input {
            width: 100%; padding: 10px 12px; font-size: 14px;
            border: 1px solid #ddd; border-radius: 8px; outline: none;
        }
        input:focus { border-color: #667eea; box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2); }
        .row { display: flex; gap: 12px; }
        .row .field { flex: 1; }
        .btn-primary {
            width: 100%; padding: 12px; border: none; border-radius: 8px;
            font-size: 15px; font-weight: 600; color: #fff; cursor: pointer;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); margin-top: 10px;
        }
        .footer-link { text-align: center; margin-top: 16px; font-size: 13px; color: #666; }
        .footer-link a { color: #667eea; text-decoration: none; font-weight: 600; }
        .lang-switch { text-align: right; margin-bottom: 12px; font-size: 12px; }
        .lang-switch a { color: #667eea; text-decoration: none; margin-left: 8px; }
    </style>
</head>
<body>
<div class="card">
    <div class="lang-switch">
        <a href="?lang=en">English</a> | <a href="?lang=km">ភាសាខ្មែរ</a>
    </div>
    <h1 class="title">ISTAD IAM</h1>
    <p class="subtitle">@if(locale.equals("km"))បង្កើតគណនីថ្មី@else Create a new account@endif</p>

    @if(error != null)
        <div class="alert-error">${error}</div>
    @endif

    <form action="/register" method="post">
        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
        <div class="row">
            <div class="field">
                <label>@if(locale.equals("km"))នាមខ្លួន@else Given Name@endif</label>
                <input type="text" name="givenName" required>
            </div>
            <div class="field">
                <label>@if(locale.equals("km"))នាមត្រកូល@else Family Name@endif</label>
                <input type="text" name="familyName" required>
            </div>
        </div>
        <div class="field">
            <label>@if(locale.equals("km"))ឈ្មោះអ្នកប្រើ@else Username@endif</label>
            <input type="text" name="username" required>
        </div>
        <div class="field">
            <label>@if(locale.equals("km"))អ៊ីមែល@else Email@endif</label>
            <input type="email" name="email" required>
        </div>
        <div class="field">
            <label>@if(locale.equals("km"))ពាក្យសម្ងាត់@else Password@endif</label>
            <input type="password" name="password" required>
        </div>
        <div class="field">
            <label>@if(locale.equals("km"))បញ្ជាក់ពាក្យសម្ងាត់@else Confirm Password@endif</label>
            <input type="password" name="confirmPassword" required>
        </div>
        <button type="submit" class="btn-primary">@if(locale.equals("km"))ចុះឈ្មោះ@else Create Account@endif</button>
    </form>
    <div class="footer-link">
        @if(locale.equals("km"))មានគណនីរួចហើយ?@else Already have an account?@endif
        <a href="/login">@if(locale.equals("km"))ចូលគណនី@else Sign in@endif</a>
    </div>
</div>
</body>
</html>

