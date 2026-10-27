
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";
const API_URL=`${API_BASE_URL}/api/employees`;

async function request(url, options = {}) {
  let response;
  try{
  response = await fetch(url, options);
  /*
  console.log(typeof(response));
  console.log(response);
  console.log(response.json())
  const msg=await response.text();
  console.log(msg);
  console.log(typeof(msg));
  /
  8
  */
  }catch{
    throw new Error("Cannot connect to the Backend.Check That Spring boot is running");
  }

  const contentType=response.headers.get("content-type")|| "";
  let data=null;

  if (contentType.includes("application/json")){
    try{
      data=await response.json();
    }catch{
      data=null;
    }
  }else{
    const text=await response.text();
    data=text? {message: text}:null;
  }


  if (!response.ok) {
    const message = data?.message||
                    data?.detail ||
                    data?.title ||
                    `Request failed with Status ${response.status}`;
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return data;
}

export function getEmployees() {
  return request(API_URL);
}

export function createEmployee(employee) {
  return request(API_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(employee),
  });
}

export function updateEmployee(id, employee) {
  return request(`${API_URL}/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(employee),
  });
}

export function deleteEmployee(id) {
  return request(`${API_URL}/${id}`, {
    method: "DELETE",
  });
}