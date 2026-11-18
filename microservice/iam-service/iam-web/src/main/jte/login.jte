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
        :root {
            --primary: #4f46e5;
            --primary-dark: #4338ca;
            --bg: #f8fafc;
            --card: #ffffff;
            --text: #0f172a;
            --muted: #64748b;
            --border: #e2e8f0;
            --input: #ffffff;
            --ring: rgba(79, 70, 229, 0.18);
        }
        @media (prefers-color-scheme: dark) {
            :root {
                --bg: #0b1020;
                --card: #121933;
                --text: #e5e9f7;
                --muted: #9aa4c2;
                --border: #27305a;
                --input: #0e1530;
                --ring: rgba(129, 140, 248, 0.3);
            }
        }

        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
            font-family: "Google Sans", "Kantumruy Pro", "Segoe UI", system-ui, -apple-system, "Khmer OS", sans-serif;
            color: var(--text);
            background-color: var(--bg);
            background-image:
                radial-gradient(600px 420px at 12% 8%, rgba(99, 102, 241, 0.20), transparent 60%),
                radial-gradient(600px 420px at 92% 92%, rgba(124, 58, 237, 0.16), transparent 60%);
            background-attachment: fixed;
            -webkit-font-smoothing: antialiased;
        }

        .card {
            width: 100%;
            max-width: 460px;
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 28px;
            padding: 40px 40px 34px;
            box-shadow: 0 30px 60px -22px rgba(49, 46, 129, 0.35);
            animation: cardIn .4s cubic-bezier(.16, 1, .3, 1);
        }
        @keyframes cardIn {
            from { opacity: 0; transform: translateY(14px); }
            to   { opacity: 1; transform: translateY(0); }
        }

        .title {
            text-align: center;
            font-size: 30px;
            font-weight: 800;
            letter-spacing: -0.03em;
            margin-bottom: 6px;
            background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
            -webkit-background-clip: text;
            background-clip: text;
            -webkit-text-fill-color: transparent;
        }

        .subtitle {
            text-align: center;
            color: var(--muted);
            font-size: 15px;
            margin: 0 0 28px;
        }

        .alert {
            padding: 12px 14px;
            border-radius: 12px;
            font-size: 14px;
            margin-bottom: 18px;
        }
        .alert-error, .alert-success { padding: 12px 14px; border-radius: 12px; font-size: 14px; margin-bottom: 18px; }
        .alert-error   { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }
        .alert-success { background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; }

        .field { margin-bottom: 18px; }

        label {
            display: block;
            font-size: 14px;
            font-weight: 600;
            color: var(--text);
            margin-bottom: 7px;
        }

        input:not([type="hidden"]) {
            width: 100%;
            padding: 12px 16px;
            font-size: 16px;
            font-family: inherit;
            color: var(--text);
            background: var(--input);
            border: 1px solid var(--border);
            border-radius: 14px;
            outline: none;
            transition: border-color .2s, box-shadow .2s;
        }
        input::placeholder { color: var(--muted); opacity: .7; }
        input:not([type="hidden"]):hover { border-color: #a5b4fc; }
        input:not([type="hidden"]):focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 4px var(--ring);
        }

        .password-wrap { position: relative; }
        .password-wrap input { padding-right: 70px; }

        .toggle {
            position: absolute;
            right: 14px;
            top: 50%;
            transform: translateY(-50%);
            background: none;
            border: none;
            color: var(--primary);
            font-size: 13px;
            font-weight: 700;
            font-family: inherit;
            cursor: pointer;
            padding: 4px 6px;
            border-radius: 8px;
        }
        .toggle:hover { background: var(--ring); }

        .btn-primary {
            width: 100%;
            padding: 13px;
            margin-top: 6px;
            border: none;
            border-radius: 9999px;
            font-size: 16px;
            font-weight: 700;
            font-family: inherit;
            color: #fff;
            cursor: pointer;
            background: linear-gradient(135deg, #6366f1 0%, #4f46e5 55%, #4338ca 100%);
            box-shadow: 0 10px 22px -8px rgba(79, 70, 229, 0.6);
            transition: transform .15s, box-shadow .2s, filter .2s;
        }
        .btn-primary:hover  { transform: translateY(-1px); box-shadow: 0 14px 26px -8px rgba(79, 70, 229, 0.7); }
        .btn-primary:active { transform: scale(.98); }

        .divider {
            display: flex;
            align-items: center;
            gap: 12px;
            color: var(--muted);
            font-size: 13px;
            margin: 24px 0;
        }
        .divider::before, .divider::after {
            content: "";
            flex: 1;
            height: 1px;
            background: var(--border);
        }

        .btn-social {
            display: block;
            width: 100%;
            padding: 12px;
            margin-bottom: 10px;
            text-align: center;
            text-decoration: none;
            font-size: 15px;
            font-weight: 600;
            color: var(--text);
            background: var(--card);
            border: 1px solid var(--border);
            border-radius: 9999px;
            transition: background .2s, border-color .2s, transform .15s;
        }
        .btn-social:hover { background: rgba(99, 102, 241, 0.07); border-color: #a5b4fc; transform: translateY(-1px); }

        /* register page */
        .row { display: flex; gap: 12px; }
        .row .field { flex: 1; }
        .footer-link { text-align: center; margin-top: 20px; font-size: 14px; color: var(--muted); }
        .footer-link a { color: var(--primary); text-decoration: none; font-weight: 700; }
        .footer-link a:hover { text-decoration: underline; }
        .lang-switch { text-align: right; margin-bottom: 12px; font-size: 13px; color: var(--muted); }
        .lang-switch a { color: var(--primary); text-decoration: none; font-weight: 600; margin-left: 8px; }

        :focus-visible { outline: 2px solid var(--primary); outline-offset: 2px; }

        @media (max-width: 480px) {
            body { padding: 14px; }
            .card { padding: 28px 22px; border-radius: 22px; }
            .row { flex-direction: column; gap: 0; }
        }
        @media (prefers-reduced-motion: reduce) {
            .card { animation: none; }
            * { transition: none !important; }
        }
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
    <a class="btn-social" href="/oauth2/authorization/facebook">Continue with Facebook</a>
    <a class="btn-social" href="/oauth2/authorization/telegram">Continue with Telegram</a>
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