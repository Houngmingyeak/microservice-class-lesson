@param String title = "Connected Applications"
@param String locale = "en"
@param java.util.List apps = java.util.List.of()
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
        .app-item { display: flex; justify-content: space-between; align-items: center; padding: 12px; border-bottom: 1px solid #eee; }
        .app-name { font-weight: 600; color: #111827; }
        .btn-revoke { background: #fee2e2; color: #b91c1c; border: none; padding: 6px 12px; border-radius: 6px; cursor: pointer; font-size: 13px; font-weight: 600; }
        .btn-cancel { background: #eee; color: #333; text-decoration: none; display: inline-block; padding: 8px 16px; border-radius: 6px; margin-top: 20px; font-size: 14px; }
    </style>
</head>
<body>
<div class="card">
    <h1>@if(locale.equals("km"))កម្មវិធីដែលបានអនុញ្ញាត@else Authorized Applications@endif</h1>

    @if(apps.isEmpty())
        <p style="color: #666; font-size: 14px;">@if(locale.equals("km"))គ្មានកម្មវិធីដែលបានភ្ជាប់នៅឡើយទេ។@else No third-party applications authorized.@endif</p>
    @else
        @for(Object app : apps)
            <div class="app-item">
                <span class="app-name">${app.toString()}</span>
            </div>
        @endfor
    @endif

    <div><a href="/" class="btn-cancel">@if(locale.equals("km"))ត្រឡប់ក្រោយ@else Back to Profile@endif</a></div>
</div>
</body>
</html>

