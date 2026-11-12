@param String title = "Linked Accounts"
@param String locale = "en"
@param java.util.List identities = java.util.List.of()
@param String csrfParameterName = "_csrf"
@param String csrfToken = ""

<!DOCTYPE html>
<html lang="${locale}">
<head>
    <meta charset="UTF-8">
    <title>${title} - ISTAD IAM</title>
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: "Segoe UI", system-ui, sans-serif; background: #f4f6f9; padding: 30px; }
        .card { max-width: 600px; margin: 0 auto; background: #fff; border-radius: 12px; padding: 30px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
        h1 { font-size: 22px; color: #667eea; margin-bottom: 20px; }
        .account-item { display: flex; justify-content: space-between; align-items: center; padding: 12px; border-bottom: 1px solid #eee; }
        .provider-badge { font-weight: 600; color: #3730a3; background: #e0e7ff; padding: 4px 10px; border-radius: 6px; font-size: 13px; }
        .btn-unlink { background: #fee2e2; color: #b91c1c; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 600; }
        .btn-connect { display: inline-block; padding: 8px 14px; border: 1px solid #ddd; border-radius: 6px; text-decoration: none; color: #333; font-size: 13px; margin: 6px 4px; }
        .btn-cancel { background: #eee; color: #333; text-decoration: none; display: inline-block; padding: 8px 16px; border-radius: 6px; margin-top: 20px; font-size: 14px; }
    </style>
</head>
<body>
<div class="card">
    <h1>@if(locale.equals("km"))គណនីដែលបានភ្ជាប់@else Linked Social Accounts@endif</h1>

    @if(identities.isEmpty())
        <p style="color: #666; font-size: 14px; margin-bottom: 20px;">@if(locale.equals("km"))មិនទាន់មានគណនីណាមួយត្រូវបានភ្ជាប់នៅឡើយទេ។@else No social accounts linked yet.@endif</p>
    @else
        @for(Object id : identities)
            <div class="account-item">
                <span class="provider-badge">${id.toString()}</span>
            </div>
        @endfor
    @endif

    <h3 style="font-size: 15px; margin: 20px 0 10px;">@if(locale.equals("km"))ភ្ជាប់គណនីបន្ថែម@else Connect another provider@endif</h3>
    <div>
        <a class="btn-connect" href="/oauth2/authorization/google">Google</a>
        <a class="btn-connect" href="/oauth2/authorization/github">GitHub</a>
        <a class="btn-connect" href="/oauth2/authorization/facebook">Facebook</a>
        <a class="btn-connect" href="/oauth2/authorization/telegram">Telegram</a>
    </div>

    <div><a href="/" class="btn-cancel">@if(locale.equals("km"))ត្រឡប់ក្រោយ@else Back to Profile@endif</a></div>
</div>
</body>
</html>

