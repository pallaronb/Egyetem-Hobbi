//Kernel for the horizontal SAT creation
__kernel void simple_sat_horizontal(__global float4* input, __global float4* horizontal,
                                   int width, int height) {
    int y = get_global_id(0);
    if (y >= height) return;
    
    float4 sum = (float4)(0.0f);
    for (int x = 0; x < width; x++) {
        int idx = y * width + x;
        sum += input[idx];
        horizontal[idx] = sum;
    }
}
//Kernel for vertical SAT creation
__kernel void simple_sat_vertical(__global float4* horizontal, __global float4* sat,
                                 int width, int height) {
    int x = get_global_id(0);
    if (x >= width) return;
    
    float4 sum = (float4)(0.0f);
    for (int y = 0; y < height; y++) {
        int idx = y * width + x;
        sum += horizontal[idx];
        sat[idx] = sum;  // Final SAT value
    }
}

//Alternative kernel for applying blur
__kernel void apply_depth_blur_simple(__global float4* col, __global float* dep, 
                                     __global float4* sat, __write_only image2d_t out, 
                                     int w, int h, float scale, int invertDepth) {
    int x = get_global_id(0), y = get_global_id(1);
    if (x >= w || y >= h) return;

    float depth_val = dep[y * w + x];
    if (invertDepth != 0) depth_val = 1.0f - depth_val;
    
    int r = max(0, (int)(depth_val * scale + 0.5f));
    
    if (r == 0) {
        float4 original = col[y * w + x];
        write_imagef(out, (int2)(x, y), (float4)(clamp(original.xyz, 0.0f, 1.0f), 1.0f));
        return;
    }
    
    // Rectangle bounds (inclusive)
    int left = max(0, x - r);
    int right = min(w - 1, x + r);
    int top = max(0, y - r);
    int bottom = min(h - 1, y + r);
    
    // SAT indices (need to handle edges)
    int br_idx = bottom * w + right;
    float4 br = sat[br_idx];
    
    // For top-1, left-1 coordinates
    int tr_idx = (top > 0) ? (top - 1) * w + right : -1;
    int bl_idx = (left > 0) ? bottom * w + (left - 1) : -1;
    int tl_idx = (top > 0 && left > 0) ? (top - 1) * w + (left - 1) : -1;
    
    float4 tr = (tr_idx >= 0) ? sat[tr_idx] : (float4)(0.0f);
    float4 bl = (bl_idx >= 0) ? sat[bl_idx] : (float4)(0.0f);
    float4 tl = (tl_idx >= 0) ? sat[tl_idx] : (float4)(0.0f);
    
    float4 sum = br - tr - bl + tl;
    
    float area = (float)((right - left + 1) * (bottom - top + 1));
    float4 average = sum / max(area, 1.0f);
    
    write_imagef(out, (int2)(x, y), (float4)(clamp(average.xyz, 0.0f, 1.0f), 1.0f));
}

// Simple box blur for comparison
__kernel void simple_box_blur(__global float4* col, __global float* dep,
                             __write_only image2d_t out, int w, int h, 
                             float scale, int invertDepth) {
    int x = get_global_id(0), y = get_global_id(1);
    if (x >= w || y >= h) return;
    
    float depth_val = dep[y * w + x];
    if (invertDepth != 0) depth_val = 1.0f - depth_val;
    
    int r = max(0, (int)(depth_val * scale + 0.5f));
    
    if (r == 0) {
        float4 original = col[y * w + x];
        write_imagef(out, (int2)(x, y), (float4)(clamp(original.xyz, 0.0f, 1.0f), 1.0f));
        return;
    }
    
    float4 sum = (float4)(0.0f);
    int count = 0;
    
    for (int dy = -r; dy <= r; dy++) {
        int ny = y + dy;
        if (ny < 0 || ny >= h) continue;
        
        for (int dx = -r; dx <= r; dx++) {
            int nx = x + dx;
            if (nx < 0 || nx >= w) continue;
            
            sum += col[ny * w + nx];
            count++;
        }
    }
    
    if (count > 0) {
        float4 average = sum / (float)count;
        write_imagef(out, (int2)(x, y), (float4)(clamp(average.xyz, 0.0f, 1.0f), 1.0f));
    } else {
        float4 original = col[y * w + x];
        write_imagef(out, (int2)(x, y), (float4)(clamp(original.xyz, 0.0f, 1.0f), 1.0f));
    }
}

// Visualize depth map
__kernel void visualize_depth(__global float* depth, __write_only image2d_t out, 
                             int w, int h, int invertDepth) {
    int x = get_global_id(0), y = get_global_id(1);
    if (x >= w || y >= h) return;
    
    float d = depth[y * w + x];
    if (invertDepth != 0) d = 1.0f - d;
    
    write_imagef(out, (int2)(x, y), (float4)(d, d, d, 1.0f));
}