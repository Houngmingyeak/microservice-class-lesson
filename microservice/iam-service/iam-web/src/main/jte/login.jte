@param String title
@param String error
@param String locale
<!DOCTYPE html>
<html lang="${locale != null ? locale : "en"}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title}</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body {
            font-family: 'Segoe UI', system-ui, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0,0,0,.3);
            width: 420px;
            padding: 48px 40px;
        }
        .logo { text-align: center; margin-bottom: 32px; }
        .logo h1 { font-size: 28px; font-weight: 700; color: #667eea; }
        .logo p { color: #6b7280; font-size: 14px; margin-top: 4px; }
        .form-group { margin-bottom: 20px; }
        label { display: block; font-size: 14px; font-weight: 500; color: #374151; margin-bottom: 6px; }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px 16px;
            border: 1.5px solid #e5e7eb;
            border-radius: 8px;
            font-size: 15px;
            transition: border-color .2s;
            outline: none;
        }
        input:focus { border-color: #667eea; }
        .btn-primary {
            width: 100%;
            padding: 13px;
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            margin-top: 8px;
            transition: opacity .2s;
        }
        .btn-primary:hover { opacity: .9; }
        .divider { text-align: center; margin: 24px 0; color: #9ca3af; font-size: 14px; position: relative; }
        .divider::before, .divider::after {
            content: '';
            position: absolute;
            top: 50%;
            width: 40%;
            height: 1px;
            background: #e5e7eb;
        }
        .divider::before { left: 0; }
        .divider::after { right: 0; }
        .social-buttons { display: flex; flex-direction: column; gap: 12px; }
        .btn-social {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            padding: 11px;
            border: 1.5px solid #e5e7eb;
            border-radius: 8px;
            font-size: 14px;
            font-weight: 500;
            text-decoration: none;
            color: #374151;
            transition: background .2s;
        }
        .btn-social:hover { background: #f9fafb; }
        .error-msg {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #dc2626;
            padding: 12px 16px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 20px;
        }
        .lang-switcher { text-align: right; margin-bottom: 24px; }
        .lang-switcher a { font-size: 13px; color: #667eea; text-decoration: none; margin-left: 8px; }
    </style>
</head>
<body>
<div class="card">
    <div class="lang-switcher">
        <a href="?lang=en">EN</a>
        <a href="?lang=km">ខ្មែរ</a>
    </div>
    <div class="logo">
        <h1>ISTAD IAM</h1>
        @if(locale != null && locale.equals("km"))
        <p>ចូលគណនី</p>
        @else
        <p>Sign in to your account</p>
        @endif
    </div>

    @if(error != null)
    <div class="error-msg">
        @if(locale != null && locale.equals("km"))
        ឈ្មោះអ្នកប្រើ ឬ ពាក្យសម្ងាត់មិនត្រឹមត្រូវ
        @else
        Invalid username or password. Please try again.
        @endif
    </div>
    @endif

    <form action="/login" method="post">
        <div class="form-group">
            <label for="username">
                @if(locale != null && locale.equals("km"))ឈ្មោះអ្នកប្រើ / អ៊ីមែល@else Username / Email@endif
            </label>
            <input type="text" id="username" name="username" required
                   placeholder="@if(locale != null && locale.equals("km"))បញ្ចូលឈ្មោះអ្នកប្រើ@else Enter username@endif">
        </div>
        <div class="form-group">
            <label for="password">
                @if(locale != null && locale.equals("km"))ពាក្យសម្ងាត់@else Password@endif
            </label>
            <input type="password" id="password" name="password" required
                   placeholder="@if(locale != null && locale.equals("km"))បញ្ចូលពាក្យសម្ងាត់@else Enter password@endif">
        </div>
        <button type="submit" class="btn-primary">
            @if(locale != null && locale.equals("km"))ចូល@else Sign In@endif
        </button>
    </form>

    <div class="divider">
        @if(locale != null && locale.equals("km"))ឬ@else or@endif
    </div>

    <div class="social-buttons">
        <a href="/oauth2/authorization/google" class="btn-social">
            🔵 @if(locale != null && locale.equals("km"))ចូលជាមួយ Google@else Continue with Google@endif
        </a>
        <a href="/oauth2/authorization/github" class="btn-social">
            ⚫ @if(locale != null && locale.equals("km"))ចូលជាមួយ GitHub@else Continue with GitHub@endif
        </a>
    </div>
</div>
</body>
</html>

