import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { LogoutButton } from "../components/LogoutButton";

export default function PropertyDetailPage() {
    const { id } = useParams(); // 从 URL 获取房源 ID
    const [property, setProperty] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/login");
            return;
        }

        fetch(`http://localhost:8080/api/properties/${id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then(async (res) => {
                if (!res.ok) {
                    const error = await res.json();
                    throw new Error(error.error || "加载失败");
                }
                return res.json();
            })
            .then((data) => setProperty(data))
            .catch((err) => console.error("房源详情加载失败", err.message));
    }, [id, navigate]);

    if (!property) {
        return <div className="p-6">加载中...</div>;
    }

    return (
        <div className="p-6">
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold">{property.title}</h2>
                <LogoutButton />
            </div>

            <div className="border rounded-2xl p-6 shadow">
                <p className="text-gray-700">
                    <strong>地址：</strong> {property.city}
                </p>
                <p className="text-gray-700 mt-2">
                    <strong>租金：</strong>{" "}
                    <span className="text-green-600 font-bold">
            ${property.rent}/月
          </span>
                </p>
                <p className="text-gray-700 mt-2">
                    <strong>描述：</strong>{" "}
                    {property.description || "暂无描述"}
                </p>
            </div>

            <button
                onClick={() => navigate(-1)}
                className="mt-6 px-4 py-2 border rounded hover:bg-gray-100"
            >
                返回
            </button>
        </div>
    );
}
