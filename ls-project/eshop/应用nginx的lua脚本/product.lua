local cjson = require("cjson")
local producer = require("resty.kafka.producer")

local broker_list = {
    { host = "192.168.31.187", port = 9092 },
    { host = "192.168.31.19", port = 9092 },
    { host = "192.168.31.227", port = 9092 }
}

local log_json = {}
log_json["request_module"] = "product_detail_info"
log_json["headers"] = ngx.req.get_headers()
log_json["uri_args"] = ngx.req.get_uri_args()
log_json["body"] = ngx.req.read_body()
log_json["http_version"] = ngx.req.http_version()
log_json["method"] = ngx.req.get_method()
log_json["raw_header"] = ngx.req.raw_header()
log_json["body_data"] = ngx.req.get_body_data()

local message = cjson.encode(log_json)

local uri_args = ngx.req.get_uri_args()
local productId = uri_args["productId"]
local shopId = uri_args["shopId"]

local async_producer = producer:new(broker_list, { producer_type = "async" })
local ok, err = async_producer:send("access-log", productId, message)

if not ok then  
    ngx.log(ngx.ERR, "kafka send err:", err)  
    return  
end

local cache_ngx = ngx.shared.my_cache

local productCacheKey = "product_info_"..productId
local shopCacheKey = "shop_info_"..shopId

local productCache = cache_ngx:get(productCacheKey)
local shopCache = cache_ngx:get(shopCacheKey)

if productCache == "" or productCache == nil then
  local http = require("resty.http")
  local httpc = http.new()

  local resp,err = httpc:request_uri("http://192.168.31.179:8080",{
    method = "GET",
    path = "/getProductInfo?productId="..productId
  })

  productCache = resp.body

  math.randomseed(tostring(os.time()):reverse():sub(1, 7))
  local expireTime = math.random(600, 1200)  

  cache_ngx:set(productCacheKey, productCache, expireTime)
end

if shopCache == "" or shopCache == nil then
  local http = require("resty.http")
  local httpc = http.new()

  local resp,err = httpc:request_uri("http://192.168.31.179:8080",{
    method = "GET",
    path = "/getShopInfo?shopId="..shopId
  })

  shopCache = resp.body

  cache_ngx:set(shopCacheKey, shopCache, 10 * 60)
end

local productCacheJSON = cjson.decode(productCache)
local shopCacheJSON = cjson.decode(shopCache)

local context = {
  productId = productCacheJSON.id,
  productName = productCacheJSON.name,
  productPrice = productCacheJSON.price,
  productPictureList = productCacheJSON.pictureList,
  productSpecification = productCacheJSON.specification,
  productService = productCacheJSON.service,
  productColor = productCacheJSON.color,
  productSize = productCacheJSON.size,
  shopId = shopCacheJSON.id,
  shopName = shopCacheJSON.name,
  shopLevel = shopCacheJSON.level,
  shopGoodCommentRate = shopCacheJSON.goodCommentRate
}

local template = require("resty.template")
template.render("product.html", context)
