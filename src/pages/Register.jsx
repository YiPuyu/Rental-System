import React, { useState } from "react";

export default function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [role, setRole] = useState("tenant"); // 默认租客

    const handleSubmit = async (e) => {
        e.preventDefault();

        const body = { username, password, email, role };

        try {
            const res = await fetch("http://localhost:8080/api/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body),
            });

            if (!res.ok) {
                const err = await res.json();
                throw new Error(err.message || "注册失败");
            }

            alert("注册成功，请登录！");
        } catch (err) {
            console.error(err);
            alert(err.message);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-4 space-y-4">
            <input
                type="text"
                placeholder="用户名"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="border p-2 w-full"
                required
            />
            <input
                type="email"
                placeholder="邮箱"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="border p-2 w-full"
            />
            <input
                type="password"
                placeholder="密码"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="border p-2 w-full"
                required
            />

            {/* 选择角色 */}
            <select
                value={role}
                onChange={(e) => setRole(e.target.value)}
                className="border p-2 w-full"
            >
                <option value="tenant">租客</option>
                <option value="landlord">房东</option>
                <option value="admin">管理员</option>
            </select>

            <button type="submit" className="px-4 py-2 bg-blue-500 text-white rounded">
                注册
            </button>
        </form>
    );
}
