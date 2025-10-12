import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function LoginPage() {
    const navigate = useNavigate();
    const [form, setForm] = useState({
        username: "",
        password: ""
    });

    const handleChange = (e) => {
        setForm({ ...form, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const res = await fetch("http://localhost:8080/api/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(form)
        });

        if (res.ok) {
            const data = await res.json();
            // 🔹 从 data.data 里取 token/role/userId
            localStorage.setItem("token", data.data.token);
            localStorage.setItem("role", data.data.role);
            localStorage.setItem("userId", data.data.userId);

            alert("登录成功！");
            navigate("/houses");
        } else {
            alert("登录失败，请检查用户名或密码！");
        }
    };


    return (
        <div>
            <h2>登录</h2>
            <form onSubmit={handleSubmit}>
                <input name="username" placeholder="用户名" onChange={handleChange} required />
                <input name="password" type="password" placeholder="密码" onChange={handleChange} required />
                <button type="submit">登录</button>
            </form>
        </div>
    );
}
