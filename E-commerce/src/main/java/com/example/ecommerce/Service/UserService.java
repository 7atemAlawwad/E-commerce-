package com.example.ecommerce.Service;

import com.example.ecommerce.Model.MerchantStock;
import com.example.ecommerce.Model.Product;
import com.example.ecommerce.Model.User;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {
    ArrayList<User> userList = new ArrayList<>();

    private final MerchantService merchantList;
    private final ProductService productList;
    private final CategoryService categoryList;
    private final MerchantStockService merchantStockList;

    ArrayList<String> waitUserIds = new ArrayList<>();
    ArrayList<String> waitProductIds = new ArrayList<>();
    ArrayList<String> waitMerchantIds = new ArrayList<>();
    ArrayList<Boolean> waitAutoBuys = new ArrayList<>();

    private static final int POINTS_PER_CURRENCY = 1;
    private static final int POINTS_PER_UNIT     = 100;


    public ArrayList<User> getAllUserList() {
        return userList;
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public boolean updateUser(User user, String id) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(id)) {
                userList.set(i, user);
                return true;
            }

        }
        return false;
    }

    public boolean removeUser(String id) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(id)) {
                userList.remove(i);
                return true;
            }
        }
        return false;
    }


    public int buyDirect(String userId, String productId, String merchantId) {
        int userIdx = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(userId)) {
                userIdx = i;
                break;
            }
        }
        if (userIdx == -1) return 0;

        int productIdx = -1;
        for (int i = 0; i < productList.products.size(); i++) {
            if (productList.products.get(i).getId().equals(productId)) {
                productIdx = i;
                break;
            }
        }
        if (productIdx == -1) return -1;

        int merchantIdx = -1;
        for (int i = 0; i < merchantList.merchants.size(); i++) {
            if (merchantList.merchants.get(i).getId().equals(merchantId)) {
                merchantIdx = i;
                break;
            }
        }
        if (merchantIdx == -1) return -2;

        int stockIdx = -1;
        for (int i = 0; i < merchantStockList.merchantStocks.size(); i++) {
            if (merchantStockList.merchantStocks.get(i).getMerchantid().equals(merchantId) &&
                    merchantStockList.merchantStocks.get(i).getProductid().equals(productId)) {
                stockIdx = i;
                break;
            }
        }
        if (stockIdx == -1) return -5;

        if (merchantStockList.merchantStocks.get(stockIdx).getStock() <= 0) return -3;


        if (userList.get(userIdx).getBalance() < productList.products.get(productIdx).getPrice()) return -4;

        if (merchantList.isOnVacation(merchantId)) return -6;


        double price = productList.products.get(productIdx).getPrice();


        MerchantStock ms = merchantStockList.merchantStocks.get(stockIdx);
        ms.setStock(ms.getStock() - 1);
        merchantStockList.merchantStocks.set(stockIdx, ms);

        User u = userList.get(userIdx);
        u.setBalance(u.getBalance() - price);

        int earned = (int) (price * POINTS_PER_CURRENCY);
        u.setPoints(u.getPoints() + earned);
        userList.set(userIdx, u);

        return 1;

    }


    public int buyRandomInRange(String userId, String categoryId, double minPrice, double maxPrice) {

        User user = null;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(userId)) {
                user = userList.get(i);
                break;
            }
        }

        if (user == null) return 0;

        boolean categoryFound = false;
        for (int i = 0; i < categoryList.categories.size(); i++) {
            if (categoryList.categories.get(i).getId().equals(categoryId)) {
                categoryFound = true;
                break;
            }
        }
        if (!categoryFound) return -1;

        ArrayList<Product> candidates = new ArrayList<>();
        for (int i = 0; i < productList.products.size(); i++) {
            Product p = productList.products.get(i);
            if (p.getCategoryID().equals(categoryId)
                    && p.getPrice() >= minPrice
                    && p.getPrice() <= maxPrice) {

                boolean available = false;
                for (int j = 0; j < merchantStockList.merchantStocks.size(); j++) {
                    MerchantStock ms = merchantStockList.merchantStocks.get(j);
                    if (ms.getProductid().equals(p.getId()) && ms.getStock() > 0) {
                        available = true;
                        break;
                    }
                }
                if (available) {
                    candidates.add(p);
                }
            }
        }
        if (candidates.isEmpty()) return -2;

        int randomIndex = (int) (Math.random() * candidates.size());
        Product chosen = candidates.get(randomIndex);

        int stockIdx = -1;
        for (int i = 0; i < merchantStockList.merchantStocks.size(); i++) {
            MerchantStock ms = merchantStockList.merchantStocks.get(i);
            if (ms.getProductid().equals(chosen.getId()) && ms.getStock() > 0) {
                stockIdx = i;
                break;
            }
        }
        if (stockIdx == -1) return -3;

        if (user.getBalance() < chosen.getPrice()) return -4;

        MerchantStock ms = merchantStockList.merchantStocks.get(stockIdx);
        ms.setStock(ms.getStock() - 1);
        merchantStockList.merchantStocks.set(stockIdx, ms);

        user.setBalance(user.getBalance() - chosen.getPrice());

        int earned = (int) (chosen.getPrice() * POINTS_PER_CURRENCY);
        user.setPoints(user.getPoints() + earned);
        return 1;


    }


    public int joinWaitlist(String userId, String productId, String merchantId, boolean autoBuy) {

        boolean userFound = false;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(userId)) {
                userFound = true;
                break;
            }
        }
        if (!userFound) return 0;

        boolean productFound = false;
        for (int i = 0; i < productList.products.size(); i++) {
            if (productList.products.get(i).getId().equals(productId)) {
                productFound = true;
                break;
            }
        }
        if (!productFound) return -1;

        boolean merchantFound = false;
        for (int i = 0; i < merchantList.merchants.size(); i++) {
            if (merchantList.merchants.get(i).getId().equals(merchantId)) {
                merchantFound = true;
                break;
            }
        }
        if (!merchantFound) return -2;

        for (int i = 0; i < waitUserIds.size(); i++) {
            if (waitUserIds.get(i).equals(userId) &&
                    waitProductIds.get(i).equals(productId) &&
                    waitMerchantIds.get(i).equals(merchantId)) {
                return 2;
            }
        }

        waitUserIds.add(userId);
        waitProductIds.add(productId);
        waitMerchantIds.add(merchantId);
        waitAutoBuys.add(autoBuy);

        return 1;
    }

    public void processWaitlist(String productId, String merchantId) {
        for (int i = 0; i < waitUserIds.size(); i++) {

            if (waitProductIds.get(i).equals(productId) &&
                    waitMerchantIds.get(i).equals(merchantId)) {

                boolean autoBuy = waitAutoBuys.get(i);
                if (!autoBuy) {
                    continue;
                }

                if (buyDirect(waitUserIds.get(i), productId, merchantId) == 1) {
                    waitUserIds.remove(i);
                    waitProductIds.remove(i);
                    waitMerchantIds.remove(i);
                    waitAutoBuys.remove(i);
                    i--;
                }
            }
        }
    }

    //  1  = success
//  0  = product not found
// -1  = merchant not found
// -2  = stock row missing
// -3  = no stock (<=0)
// -4  = userIds/amounts size mismatch or empty
// -5  = sum of amounts != product price
// -6  = one or more users not found
// -7  = one or more users have insufficient balance
    public int splitBuyOneItem(String productId,
                               String merchantId,
                               ArrayList<String> userIds,
                               ArrayList<Double> amounts) {

        Product product = null;
        for (int i = 0; i < productList.products.size(); i++) {
            if (productList.products.get(i).getId().equals(productId)) {
                product = productList.products.get(i);
                break;
            }
        }
        if (product == null) return 0;

        boolean merchantFound = false;
        for (int i = 0; i < merchantList.merchants.size(); i++) {
            if (merchantList.merchants.get(i).getId().equals(merchantId)) {
                merchantFound = true;
                break;
            }
        }
        if (!merchantFound) return -1;

        int stockIdx = -1;
        for (int i = 0; i < merchantStockList.merchantStocks.size(); i++) {
            MerchantStock ms = merchantStockList.merchantStocks.get(i);
            if (ms.getMerchantid().equals(merchantId) && ms.getProductid().equals(productId)) {
                stockIdx = i;
                break;
            }
        }
        if (stockIdx == -1)
            return -2;

        if (merchantStockList.merchantStocks.get(stockIdx).getStock() <= 0)
            return -3;

        if (userIds == null || amounts == null || userIds.size() == 0 || userIds.size() != amounts.size())
            return -4;

        double sum = 0.0;
        for (int i = 0; i < amounts.size(); i++) {
            Double v = amounts.get(i);
            if (v == null || v <= 0) return -5;
            sum += v;
        }
        if (Math.abs(sum - product.getPrice()) > 0.0001) return -5;

        int[] userIdxs = new int[userIds.size()];
        for (int i = 0; i < userIds.size(); i++) {
            String uid = userIds.get(i);

            int foundIdx = -1;
            for (int j = 0; j < userList.size(); j++) {
                if (userList.get(j).getId().equals(uid)) {
                    foundIdx = j;
                    break;
                }
            }
            if (foundIdx == -1) return -6;

            if (userList.get(foundIdx).getBalance() < amounts.get(i)) return -7;

            userIdxs[i] = foundIdx;
        }

        for (int i = 0; i < userIds.size(); i++) {
            double part = amounts.get(i);
            userList.get(userIdxs[i]).setBalance(userList.get(userIdxs[i]).getBalance() - part);
            int earned = (int) (part * POINTS_PER_CURRENCY);
            userList.get(userIdxs[i]).setPoints(userList.get(userIdxs[i]).getPoints() + earned);
            userList.set(userIdxs[i], userList.get(userIdxs[i]));
        }

        MerchantStock ms = merchantStockList.merchantStocks.get(stockIdx);
        ms.setStock(ms.getStock() - 1);
        merchantStockList.merchantStocks.set(stockIdx, ms);

        return 1;
    }



    // Codes:
    //  1  = redeemed
    //  0  = user not found
    // -1  = points <= 0
    // -2  = not enough points
    public int redeemPoints(String userId, int points) {

        if (points <= 0) return -1;

        int idx = -1;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId().equals(userId)) {
                idx = i;
                break;
            }
        }
        if (idx == -1) return 0;

        if (userList.get(idx).getPoints() < points) return -2;

        userList.get(idx).setPoints(userList.get(idx).getPoints() - points);
        double add = points / (double) POINTS_PER_UNIT;
        userList.get(idx).setBalance(userList.get(idx).getBalance() + add);

        userList.set(idx, userList.get(idx));
        return 1;
    }


    public int addMoreStock(String merchantId, String productId, int amount) {

        if (amount <= 0) return -2;

        boolean merchantFound = false;
        for (int i = 0; i < merchantList.merchants.size(); i++) {
            if (merchantList.merchants.get(i).getId().equals(merchantId)) {
                merchantFound = true;
                break;
            }
        }
        if (!merchantFound) return -1;

        boolean productFound = false;
        for (int i = 0; i < productList.products.size(); i++) {
            if (productList.products.get(i).getId().equals(productId)) {
                productFound = true;
                break;
            }
        }
        if (!productFound) return 0;

        for (int i = 0; i < merchantStockList.merchantStocks.size(); i++) {
            MerchantStock ms = merchantStockList.merchantStocks.get(i);
            if (ms.getMerchantid().equals(merchantId) && ms.getProductid().equals(productId)) {
                ms.setStock(ms.getStock() + amount);
                merchantStockList.merchantStocks.set(i, ms);

                processWaitlist(productId, merchantId);
                return 1;
            }
        }

        return 2;
    }

}
