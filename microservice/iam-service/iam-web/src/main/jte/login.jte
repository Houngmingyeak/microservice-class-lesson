@param boolean hasError = false
@param boolean loggedOut = false
@param String csrfParameterName
@param String csrfToken

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In - ISTAD IAM</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: "Segoe UI", system-ui, -apple-system, "Khmer OS", sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 20px;
        }

        .card {
            width: 100%;
            max-width: 420px;
            background: #fff;
            border-radius: 16px;
            padding: 40px 40px 32px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.25);
        }

        .title {
            text-align: center;
            font-size: 28px;
            font-weight: 700;
            color: #667eea;
        }

        .subtitle {
            text-align: center;
            color: #777;
            font-size: 14px;
            margin: 6px 0 28px;
        }

        .alert {
            padding: 12px 14px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 18px;
        }
        .alert-error   { background: #fdecea; color: #b3261e; border: 1px solid #f5c2be; }
        .alert-success { background: #e8f5e9; color: #1b5e20; border: 1px solid #c3e6c5; }

        .field { margin-bottom: 18px; }

        label {
            display: block;
            font-size: 14px;
            font-weight: 600;
            color: #333;
            margin-bottom: 6px;
        }

        input[type="text"],
        input[type="password"] {
            width: 100%;
            padding: 12px 14px;
            font-size: 15px;
            border: 1px solid #ddd;
            border-radius: 10px;
            outline: none;
            transition: border-color .2s, box-shadow .2s;
        }

        input:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.2);
        }

        .password-wrap { position: relative; }
        .password-wrap input { padding-right: 60px; }

        .toggle {
            position: absolute;
            right: 12px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: #667eea;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
        }

        .btn-primary {
            width: 100%;
            padding: 13px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            color: #fff;
            cursor: pointer;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            transition: transform .1s, opacity .2s;
        }
        .btn-primary:hover  { opacity: .92; }
        .btn-primary:active { transform: scale(.98); }

        .divider {
            display: flex;
            align-items: center;
            gap: 12px;
            color: #999;
            font-size: 13px;
            margin: 22px 0;
        }
        .divider::before, .divider::after {
            content: "";
            flex: 1;
            height: 1px;
            background: #e5e5e5;
        }

        .btn-social {
            display: block;
            width: 100%;
            padding: 12px;
            margin-bottom: 10px;
            text-align: center;
            text-decoration: none;
            font-size: 15px;
            font-weight: 500;
            color: #333;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 10px;
            transition: background .2s;
        }
        .btn-social:hover { background: #f7f7f9; }
    </style>
</head>
<body>

<div class="card">
    <h1 class="title">ISTAD IAM</h1>
    <p class="subtitle">Sign in to your account</p>

    <%-- បង្ហាញពេលបញ្ចូលពាក្យសម្ងាត់ខុស (/login?error) --%>
    @if(hasError)
        <div class="alert alert-error">
            Invalid username or password.
        </div>
    @endif

    <%-- បង្ហាញពេល logout ជោគជ័យ (/login?logout) --%>
    @if(loggedOut)
        <div class="alert alert-success">
            You have been signed out.
        </div>
    @endif

    <%-- action="/login" ត្រូវតែដូចគ្នានឹង loginPage ក្នុង SecurityConfig --%>
    <form action="/login" method="post">

        <%-- CSRF token (ចាំបាច់ បើគ្មានវា login នឹង 403) --%>
        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">

        <div class="field">
            <label for="username">Username / Email</label>
            <input type="text" id="username" name="username"
                   placeholder="Enter username" autocomplete="username"
                   required autofocus>
        </div>

        <div class="field">
            <label for="password">Password</label>
            <div class="password-wrap">
                <input type="password" id="password" name="password"
                       placeholder="Enter password" autocomplete="current-password"
                       required>
                <button type="button" class="toggle" id="toggle">Show</button>
            </div>
        </div>

        <button type="submit" class="btn-primary">Sign In</button>
    </form>

    <div class="divider">or</div>

    <a class="btn-social" href="/oauth2/authorization/google">Continue with Google</a>
    <a class="btn-social" href="/oauth2/authorization/github">Continue with GitHub</a>
</div>

<script>
    const pwd = document.getElementById('password');
    const btn = document.getElementById('toggle');
    btn.addEventListener('click', () => {
        const show = pwd.type === 'password';
        pwd.type = show ? 'text' : 'password';
        btn.textContent = show ? 'Hide' : 'Show';
    });
</script>

</body>
</html>