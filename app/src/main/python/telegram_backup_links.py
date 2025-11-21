import asyncio
import html
from telethon import TelegramClient, functions, types

async def do_backup(api_id, api_hash):
    client = TelegramClient("session", int(api_id), api_hash)
    await client.start()

    dialogs = await client.get_dialogs()
    rows = []

    for d in dialogs:
        entity = d.entity
        name = getattr(entity, "title", None) or getattr(entity, "first_name", None) or ""
        username = getattr(entity, "username", "") or ""
        link = f"https://t.me/{username}" if username else ""
        kind = type(entity).__name__

        rows.append({
            "name": name,
            "type": kind,
            "username": username,
            "link": link,
        })

    await client.disconnect()

    html_rows = []
    for i, r in enumerate(rows, 1):
        html_rows.append(
            f"<tr><td>{i}</td><td>{html.escape(r['name'])}</td>"
            f"<td>{html.escape(r['type'])}</td>"
            f"<td>{html.escape(r['username'])}</td>"
            f"<td>{html.escape(r['link'])}</td></tr>"
        )

    return """
    <html><head><meta charset="utf-8">
    <style>
    table {border-collapse: collapse; width: 100%;}
    th, td {border: 1px solid #ccc; padding: 8px;}
    th {background: #eee;}
    </style></head><body>
    <h2>Telegram Backup</h2>
    <table>
    <tr><th>#</th><th>Name</th><th>Type</th><th>Username</th><th>Link</th></tr>
    """ + "\n".join(html_rows) + "</table></body></html>"


def run_backup(api_id, api_hash):
    return asyncio.run(do_backup(api_id, api_hash))