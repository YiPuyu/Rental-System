import React from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Navbar() {
    const navigate = useNavigate();
    const token = localStorage.getItem("token");

    const handleLogout = () => {
        localStorage.removeItem("token");
        alert("已登出！");
        navigate("/login");
    };

    return (
        <nav style={{ padding: "10px", borderBottom: "1px solid #ccc" }}>
            {token ? (
                <>
                    <Link to="/houses" style={{ marginRight: "10px" }}>房源列表</Link>
                    <button onClick={handleLogout}>登出</button>
                </>
            ) : (
                <>
                    <Link to="/login" style={{ marginRight: "10px" }}>登录</Link>
                    <Link to="/register">注册</Link>
                </>
            )}
        </nav>
    );
}
