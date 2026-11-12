@param String title = "Change Password"
@param String locale = "en"
@param String success = null
@param String error = null
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
        .card { max-width: 460px; margin: 0 auto; background: #fff; border-radius: 12px; padding: 30px; box-shadow: 0 4px 12px rgba(0,0,0,0.06); }
        h1 { font-size: 22px; color: #667eea; margin-bottom: 20px; }
        .field { margin-bottom: 14px; }
        label { display: block; font-size: 13px; font-weight: 600; margin-bottom: 4px; color: #333; }
        input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; }
        .btn { padding: 10px 18px; border-radius: 8px; font-weight: 600; cursor: pointer; border: none; font-size: 14px; }
        .btn-primary { background: #667eea; color: #fff; }
        .btn-cancel { background: #eee; color: #333; text-decoration: none; display: inline-block; padding: 10px 18px; }
        .actions { display: flex; gap: 10px; margin-top: 20px; }
        .alert-success { background: #e8f5e9; color: #1b5e20; padding: 10px; border-radius: 8px; margin-bottom: 14px; font-size: 14px; }
        .alert-error { background: #fdecea; color: #b3261e; padding: 10px; border-radius: 8px; margin-bottom: 14px; font-size: 14px; }
    </style>
</head>
<body>
<div class="card">
    <h1>@if(locale.equals("km"))ប្ដូរពាក្យសម្ងាត់@else Change Password@endif</h1>
    @if(success != null)<div class="alert-success">${success}</div>@endif
    @if(error != null)<div class="alert-error">${error}</div>@endif
    <form action="/profile/password" method="post">
        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
        <div class="field">
            <label>@if(locale.equals("km"))ពាក្យសម្ងាត់បច្ចុប្បន្ន@else Current Password@endif</label>
            <input type="password" name="currentPassword" required>
        </div>
        <div class="field">
            <label>@if(locale.equals("km"))ពាក្យសម្ងាត់ថ្មី@else New Password@endif</label>
            <input type="password" name="newPassword" required>
        </div>
        <div class="field">
            <label>@if(locale.equals("km"))បញ្ជាក់ពាក្យសម្ងាត់ថ្មី@else Confirm New Password@endif</label>
            <input type="password" name="confirmPassword" required>
        </div>
        <div class="actions">
            <button type="submit" class="btn btn-primary">@if(locale.equals("km"))ប្ដូរពាក្យសម្ងាត់@else Update Password@endif</button>
            <a href="/" class="btn btn-cancel">@if(locale.equals("km"))បោះបង់@else Back@endif</a>
        </div>
    </form>
</div>
</body>
</html>

