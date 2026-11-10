@param String title
@param String locale
@param String clientName
@param String scopes
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
            display: flex; align-items: center; justify-content: center;
        }
        .card {
            background: white; border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0,0,0,.3);
            width: 440px; padding: 48px 40px;
        }
        .logo { text-align: center; margin-bottom: 32px; }
        .logo h1 { font-size: 24px; font-weight: 700; color: #667eea; }
        .client-name { font-size: 18px; font-weight: 600; color: #111827; text-align: center; margin-bottom: 8px; }
        .description { color: #6b7280; font-size: 14px; text-align: center; margin-bottom: 24px; }
        .scopes-list {
            background: #f9fafb; border: 1px solid #e5e7eb;
            border-radius: 10px; padding: 16px; margin-bottom: 28px;
        }
        .scope-item { display: flex; align-items: center; gap: 10px; padding: 6px 0; font-size: 14px; color: #374151; }
        .scope-item::before { content: '✓'; color: #10b981; font-weight: bold; }
        .btn-row { display: flex; gap: 12px; }
        .btn { flex: 1; padding: 13px; border-radius: 8px; font-size: 15px; font-weight: 600; cursor: pointer; border: none; }
        .btn-approve { background: linear-gradient(135deg, #667eea, #764ba2); color: white; }
        .btn-deny { background: #f3f4f6; color: #374151; }
        .btn:hover { opacity: .9; }
    </style>
</head>
<body>
<div class="card">
    <div class="logo"><h1>ISTAD IAM</h1></div>
    <div class="client-name">${clientName}</div>
    <div class="description">
        @if(locale != null && locale.equals("km"))
        កម្មវិធីនេះស្នើសុំការអនុញ្ញាតអ្នក
        @else
        This application is requesting access to your account
        @endif
    </div>
    <div class="scopes-list">
        @if(locale != null && locale.equals("km"))
        <div class="scope-item">ព័ត៌មានផ្ទាល់ខ្លួន (openid profile)</div>
        <div class="scope-item">អ៊ីមែល</div>
        @else
        <div class="scope-item">Personal information (openid profile)</div>
        <div class="scope-item">Email address</div>
        @endif
    </div>
    <form action="/oauth2/authorize" method="post">
        <div class="btn-row">
            <button class="btn btn-deny" type="submit" name="action" value="deny">
                @if(locale != null && locale.equals("km"))បដិសេធ@else Deny@endif
            </button>
            <button class="btn btn-approve" type="submit" name="action" value="approve">
                @if(locale != null && locale.equals("km"))អនុញ្ញាត@else Approve@endif
            </button>
        </div>
    </form>
</div>
</body>
</html>

