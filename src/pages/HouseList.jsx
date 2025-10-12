import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { LogoutButton } from '../components/LogoutButton';

export default function HousesPage() {
    const [houses, setHouses] = useState([]);
    const [keyword, setKeyword] = useState('');
    const [city, setCity] = useState('');
    const [houseType, setHouseType] = useState('');
    const [minRent, setMinRent] = useState('');
    const [maxRent, setMaxRent] = useState('');
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const size = 5;
    const navigate = useNavigate();




    const currentUserRole = (localStorage.getItem("role") || "").toUpperCase();
    const currentUserId = localStorage.getItem("userId");





    const fetchHouses = () => {
        const token = localStorage.getItem("token");
        if (!token) {
            navigate("/login");
            return;
        }

        const params = new URLSearchParams();
        if (keyword) params.append('keyword', keyword);
        if (city) params.append('city', city);
        if (houseType) params.append('houseType', houseType);
        if (minRent) params.append('minRent', minRent);
        if (maxRent) params.append('maxRent', maxRent);
        params.append('page', page);
        params.append('size', size);

        fetch(`http://localhost:8080/api/properties/search?${params.toString()}`, {
            headers: { "Authorization": `Bearer ${token}` }
        })
            .then(res => res.json())
            .then(data => {
                setHouses(data.content);
                setTotalPages(data.totalPages);
            })
            .catch(err => console.error("房源加载失败", err));
    };

    const handleDelete = async (id) => {
        if (!window.confirm("确定要删除这个房源吗？")) return;
        const token = localStorage.getItem("token");
        const res = await fetch(`http://localhost:8080/api/properties/${id}`, {
            method: 'DELETE',
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (res.ok) {
            alert("房源已删除");
            fetchHouses();
        } else {
            alert("删除失败");
        }
    };

    useEffect(() => {
        fetchHouses();
    }, [keyword, city, houseType, minRent, maxRent, page]);

    return (
        <div className="p-6">
            {/* 标题和功能按钮 */}
            <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold">房源列表</h2>
                <div className="flex gap-2 items-center">
                    {/* ✅ 仅房东或管理员可发布新房源 */}
                    {(currentUserRole === "LANDLORD" || currentUserRole === "ADMIN") && (
                        <button
                            onClick={() => navigate("/add-house")}
                            className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600"
                        >
                            发布新房源
                        </button>
                    )}
                    <LogoutButton />
                </div>
            </div>

            {/* 筛选区域 */}
            <div className="mb-4 grid grid-cols-1 md:grid-cols-3 lg:grid-cols-5 gap-2">
                <input type="text" placeholder="关键词" value={keyword} onChange={e => { setKeyword(e.target.value); setPage(0); }} className="border p-2 rounded"/>
                <input type="text" placeholder="城市" value={city} onChange={e => { setCity(e.target.value); setPage(0); }} className="border p-2 rounded"/>
                <input type="text" placeholder="房型" value={houseType} onChange={e => { setHouseType(e.target.value); setPage(0); }} className="border p-2 rounded"/>
                <input type="number" placeholder="最小租金" value={minRent} onChange={e => { setMinRent(e.target.value); setPage(0); }} className="border p-2 rounded"/>
                <input type="number" placeholder="最大租金" value={maxRent} onChange={e => { setMaxRent(e.target.value); setPage(0); }} className="border p-2 rounded"/>
            </div>

            {/* 房源列表 */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {houses.map(house => {
                    const canDelete =
                        currentUserRole === 'ADMIN' ||currentUserRole === "LANDLORD"||
                        Number(currentUserId) === house.ownerId;

                    return (
                        <div key={house.id} className="border rounded-2xl p-4 shadow hover:shadow-lg transition">
                            <img src={`https://picsum.photos/seed/${house.id}/300/200`} alt="房源图" className="rounded-t-2xl w-full h-48 object-cover"/>
                            <h3 className="text-xl font-semibold mt-2">{house.title}</h3>
                            <p className="text-gray-600">{house.address}</p>
                            <p className="text-green-600 font-bold mt-2">${house.rent}/月</p>

                            {/* ✅ 仅房东本人或管理员可删除 */}
                            {canDelete  && (
                                <button
                                    onClick={() => handleDelete(house.id)}
                                    className="mt-2 px-3 py-1 bg-red-500 text-white rounded hover:bg-red-600"
                                >
                                    删除
                                </button>
                            )}
                        </div>
                    );
                })}
            </div>

            {/* 分页 */}
            <div className="flex justify-center items-center mt-6 gap-2">
                <button onClick={() => setPage(prev => Math.max(prev - 1, 0))} disabled={page === 0} className="px-3 py-1 border rounded disabled:opacity-50">上一页</button>
                <span>第 {page + 1} / {totalPages} 页</span>
                <button onClick={() => setPage(prev => Math.min(prev + 1, totalPages - 1))} disabled={page + 1 >= totalPages} className="px-3 py-1 border rounded disabled:opacity-50">下一页</button>
            </div>
        </div>
    );
}
