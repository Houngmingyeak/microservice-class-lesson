@param String title = "My Profile"
@param String locale = "en"
@param String username = ""
@param String email = ""
@param java.util.Collection roles = java.util.List.of()
@param java.util.Collection permissions = java.util.List.of()
@param java.util.Map claims = java.util.Map.of()
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
            font-family: "Segoe UI", system-ui, -apple-system, sans-serif;
            background: #f4f6f9; color: #333; padding: 30px;
        }
        .container { max-width: 900px; margin: 0 auto; }
        .header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
        .header h1 { font-size: 26px; color: #667eea; }
        .nav-links { display: flex; gap: 14px; align-items: center; }
        .nav-links a { color: #555; text-decoration: none; font-size: 14px; font-weight: 500; }
        .nav-links a:hover { color: #667eea; }
        .card { background: #fff; border-radius: 12px; padding: 24px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); margin-bottom: 20px; }
        .card h2 { font-size: 18px; margin-bottom: 14px; color: #222; border-bottom: 1px solid #eee; padding-bottom: 8px; }
        .badge { display: inline-block; padding: 4px 10px; border-radius: 20px; font-size: 12px; font-weight: 600; margin: 3px; }
        .badge-role { background: #e0e7ff; color: #3730a3; }
        .badge-perm { background: #ecfdf5; color: #065f46; }
        .btn-logout { background: #ef4444; color: #fff; padding: 8px 16px; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; font-weight: 600; }
        .table { width: 100%; border-collapse: collapse; margin-top: 10px; font-size: 14px; }
        .table th, .table td { padding: 10px; border-bottom: 1px solid #eee; text-align: left; }
        .table th { background: #fafafa; font-weight: 600; }
    </style>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>ISTAD IAM - @if(locale.equals("km"))ប្រវត្តិរូបរបស់ខ្ញុំ@else My Profile@endif</h1>
        <div class="nav-links">
            <a href="/profile/edit">@if(locale.equals("km"))កែប្រែប្រវត្តិរូប@else Edit Profile@endif</a>
            <a href="/profile/password">@if(locale.equals("km"))ប្ដូរពាក្យសម្ងាត់@else Change Password@endif</a>
            <a href="/profile/accounts">@if(locale.equals("km"))គណនីភ្ជាប់@else Linked Accounts@endif</a>
            <a href="/profile/apps">@if(locale.equals("km"))កម្មវិធី@else Connected Apps@endif</a>
            <form action="/logout" method="post" style="display:inline;">
                <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                <button type="submit" class="btn-logout">@if(locale.equals("km"))ចាកចេញ@else Sign Out@endif</button>
            </form>
        </div>
    </div>

    <div class="card">
        <h2>@if(locale.equals("km"))ព័ត៌មានអ្នកប្រើប្រាស់@else User Information@endif</h2>
        <p><strong>@if(locale.equals("km"))ឈ្មោះអ្នកប្រើ@else Username@endif:</strong> ${username}</p>
        <p><strong>@if(locale.equals("km"))អ៊ីមែល@else Email@endif:</strong> ${email}</p>
    </div>

    <div class="card">
        <h2>@if(locale.equals("km"))តួនាទី (Roles)@else Roles@endif</h2>
        @for(Object r : roles)
            <span class="badge badge-role">${r.toString()}</span>
        @endfor
    </div>

    <div class="card">
        <h2>@if(locale.equals("km"))សិទ្ធិ (Permissions)@else Permissions@endif</h2>
        @for(Object p : permissions)
            <span class="badge badge-perm">${p.toString()}</span>
        @endfor
    </div>

    <div class="card">
        <h2>@if(locale.equals("km"))ព័ត៌មាន (Claims)@else OIDC Claims@endif</h2>
        <table class="table">
            <thead>
                <tr><th>Claim</th><th>Value</th></tr>
            </thead>
            <tbody>
                @for(java.util.Map.Entry entry : claims.entrySet())
                    <tr><td>${entry.getKey().toString()}</td><td>${entry.getValue() != null ? entry.getValue().toString() : ""}</td></tr>
                @endfor
            </tbody>
        </table>
    </div>
</div>
</body>
</html>

